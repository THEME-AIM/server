package com.aim.server.domain.openstack.service

import com.aim.server.domain.openstack.entity.CustomServer
import com.aim.server.domain.openstack.repository.CustomProjectRepository
import com.aim.server.domain.openstack.repository.CustomServerRepository
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient
import org.openstack4j.api.exceptions.AuthenticationException
import org.openstack4j.model.common.Identifier
import org.openstack4j.model.compute.*
import org.openstack4j.openstack.OSFactory
import org.springframework.beans.factory.annotation.Value
import java.time.Duration
import java.time.LocalDateTime

class CustomServerServiceImpl(
    private val customProjectRepository: CustomProjectRepository,
    private val customServerRepository: CustomServerRepository
) : CustomServerService {
    @Value("\${openstack.keystone.endpoint}")
    private lateinit var endpoint: String

    @Value("\${openstack.keystone.username}")
    private lateinit var username: String

    @Value("\${openstack.keystone.password}")
    private lateinit var password: String

    @Value("\${openstack.keystone.domain}")
    private lateinit var domain: String

    fun osAuthToken(): OSClient.OSClientV3 {
        try {
            OSFactory.enableHttpLoggingFilter(true)
            return OSFactory.builderV3()
                .endpoint(endpoint)
                .credentials(username, password, Identifier.byId(domain))
                .scopeToProject(Identifier.byName(username), Identifier.byId(domain))
                .authenticate()
        } catch (e: AuthenticationException) {
            e.printStackTrace()
        }
        throw Exception("AuthenticationException")
    }

    override fun createServer(projectId: Long, username: String) {
        val project = customProjectRepository.findCustomProjectById(projectId).orElseThrow {
            throw Exception("존재하지 않는 프로젝트입니다.")
        }
        for (i in 1..project.numOfInstances) {
            val serverName = "${username}_${project.name}_$i"
            val createServer = Builders.server()
                .name(serverName)
                .flavor(project.flavor.ordinal.toString())
                .image(getImage(project.imageName))
                .addPersonality("/etc/motd", "Welcome to the AIM Cloud")
                .blockDevice(
                    Builders.blockDeviceMapping().uuid(getImage(project.imageName).id).sourceType(BDMSourceType.IMAGE)
                        .volumeSize(1).deviceName("/dev/vda").bootIndex(0).destinationType(BDMDestType.LOCAL)
                        .deleteOnTermination(true).build()
                ).build()
            createServer.addNetwork(project.ipAddress.networkName, project.ipAddress.ipAddress)
            createServer.addNetworkPort("")
            customServerRepository.save(
                CustomServer(
                    serverName = serverName,
                    project = project,
                    enabled = true,
                    isStopped = false,
                    startTime = LocalDateTime.now(),
                    imageName = project.imageName,
                    ipAddress = project.ipAddress.ipAddress,
                    networkId = project.ipAddress.networkName,
                    networkName = project.ipAddress.networkName
                )
            )
            osAuthToken().compute().servers().boot(createServer)
        }
    }


    override fun startServer(serverName: String) {
        val findServer = customServerRepository.findCustomServerByServerName(serverName).orElseThrow {
            throw Exception("존재하지 않는 서버입니다.")
        }
        if (findServer.isStopped) {
            throw Exception("이미 실행중인 서버입니다.")
        }
        val server = getServer(serverName)
        osAuthToken().compute().servers().action(server.id, Action.START)
        customServerRepository.save(findServer.apply { isStopped = false; startTime = LocalDateTime.now() })
    }

    override fun stopServer(serverName: String) {
        val findServer = customServerRepository.findCustomServerByServerName(serverName).orElseThrow {
            throw Exception("존재하지 않는 서버입니다.")
        }
        if (!findServer.isStopped) {
            throw Exception("이미 중지된 서버입니다.")
        }
        val server = getServer(serverName)
        osAuthToken().compute().servers().action(server.id, Action.STOP)
        customServerRepository.save(findServer.apply {
            isStopped = true; upTime = Duration.between(startTime, LocalDateTime.now())
        })
    }

    override fun deleteServer(serverName: String) {
        val findServer = customServerRepository.findCustomServerByServerName(serverName).orElseThrow {
            throw Exception("존재하지 않는 서버입니다.")
        }
        if (!findServer.enabled) {
            throw Exception("이미 중지된 서버입니다.")
        }
        val server = getServer(serverName)
        osAuthToken().compute().servers().delete(server.id)
        customServerRepository.delete(findServer)
    }

    private fun getImage(name: String): Image {
        return osAuthToken().compute().images().list().find { it.name == name } ?: throw Exception("")
    }

    private fun getServer(serverName: String): Server {
        return osAuthToken().compute().servers().list().find { it.name == serverName } ?: throw Exception("")
    }
}