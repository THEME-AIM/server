package com.aim.server.domain.address.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.api.exceptions.AuthenticationException
import org.openstack4j.model.common.Identifier
import org.openstack4j.model.compute.BDMDestType
import org.openstack4j.model.compute.BDMSourceType
import org.openstack4j.model.network.AttachInterfaceType
import org.openstack4j.model.network.IPVersionType
import org.openstack4j.model.network.Network
import org.openstack4j.model.network.Subnet
import org.openstack4j.openstack.OSFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class OpenStackNetworkServiceImpl(
//    private val ipAddressRepository: IpAddressRepository,
) : OpenStackNetworkService {
    private val log = KotlinLogging.logger { }

    @Value("\${openstack.keystone.endpoint}")
    private lateinit var endpoint: String

    @Value("\${openstack.keystone.username}")
    private lateinit var username: String

    @Value("\${openstack.keystone.password}")
    private lateinit var password: String

    @Value("\${openstack.keystone.domain}")
    private lateinit var domain: String

    @Value("\${openstack.neutron.router.id}")
    private lateinit var routerId: String
    
    fun osAuthToken(): OSClientV3 {
        try {
            OSFactory.enableHttpLoggingFilter(true)
            return OSFactory.builderV3()
                .endpoint(endpoint)
                .credentials(username, password, Identifier.byId(domain))
                .scopeToProject(Identifier.byName(username), Identifier.byId(domain))
                .authenticate()
        } catch (e: AuthenticationException) {
            log.error { "AuthenticationException: ${e.message}" }
            e.printStackTrace()
        }
        throw Exception("AuthenticationException")
    }

    // TODO: startIp, endIp, cidr 이 세개를 모두 받아 네트워크와 서브넷 생성하게 수정 필요
    // TODO: 할당 시작할 경우 프로젝트 생성? 엔드 포인트 개발 필요함.
    // TODO: 만약, 비할당 할 경우에는 프로젝트 삭제 필요함.
    // 기술 자체가 네트워크 기반의 프로젝트 생성은 아닌데, 굳이 프로젝트를 생성해야하나??
    override fun createNetwork(networkName: String, ipAddress: String): Network {
//        ipAddressRepository.findByIpAddress(ipAddress).ifPresent {
//            throw IllegalArgumentException("이미 사용중인 IP 주소입니다.")
//        }
        val network = osAuthToken().networking().network().create(
            Builders.network()
                .name(networkName)
                .adminStateUp(true)
                .build()
        )
        createSubnet(network, networkName + "_subnet", ipAddress)
        return network
    }

    override fun deleteNetwork(networkName: String) {
        osAuthToken().networking().network().delete(networkName)
    }

    override fun getNetork(networkName: String): Network {
        return osAuthToken().networking().network().get(networkName)
    }

    override fun getNetworkList(): List<Network> {
        return osAuthToken().networking().network().list()
    }

    private fun createSubnet(
        network: Network,
        subnetName: String,
        startIp: String,
        endIp: String,
        cidr: String
    ): Subnet {
        val subnet = osAuthToken().networking().subnet().create(
            Builders.subnet()
                .name(subnetName)
                .networkId(network.id)
                .enableDHCP(true)
                .addPool(startIp, endIp)
                .ipVersion(IPVersionType.V4)
                .cidr(cidr)
                .build()
        )
        osAuthToken().networking().router().attachInterface(routerId, AttachInterfaceType.SUBNET, subnet.id)
        return subnet
    }

    private fun deleteSubnet(subnetName: String) {
        osAuthToken().networking().subnet().delete(subnetName)
    }

    private fun getSubnet(subnetName: String): Subnet {
        return osAuthToken().networking().subnet().get(subnetName)
    }

    private fun getSubnetList(): List<Subnet> {
        return osAuthToken().networking().subnet().list()
    }

    override fun createServer1(projectId: Long, username: String) {
        val serverName = "server-$username-$projectId"
        val createServer = Builders.server()
            .name(serverName)
            .flavor(1.toString())
            .image("68861bc8-621f-4475-852d-ff7a39bc881f")
            .addPersonality("/etc/motd", "Welcome to the AIM Cloud")
            .blockDevice(
                Builders.blockDeviceMapping().uuid("68861bc8-621f-4475-852d-ff7a39bc881f")
                    .sourceType(BDMSourceType.IMAGE)
                    .volumeSize(1).deviceName("/dev/vda").bootIndex(0).destinationType(BDMDestType.LOCAL)
                    .deleteOnTermination(true).build()
            ).build()
        createServer.addNetwork("test", "192.168.0.1")
        createServer.addNetworkPort("")
        osAuthToken().compute().servers().boot(createServer)
    }

    override fun createServer2(projectId: Long, username: String) {
        val serverName = "server-$username-$projectId"
        val createServer = Builders.server()
            .name(serverName)
            .flavor(1.toString())
            .image("68861bc8-621f-4475-852d-ff7a39bc881f")
            .addPersonality("/etc/motd", "Welcome to the AIM Cloud")
            .blockDevice(
                Builders.blockDeviceMapping().uuid("68861bc8-621f-4475-852d-ff7a39bc881f")
                    .sourceType(BDMSourceType.IMAGE)
                    .volumeSize(1).deviceName("/dev/vda").bootIndex(0).destinationType(BDMDestType.LOCAL)
                    .deleteOnTermination(true).build()
            ).build()
        createServer.addNetwork("test", "192.168.0.1")
        createServer.addNetworkPort("")
        osAuthToken().compute().servers().boot(createServer)
    }
}