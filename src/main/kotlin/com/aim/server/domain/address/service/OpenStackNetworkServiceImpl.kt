package com.aim.server.domain.address.service

import com.aim.server.domain.admin.const.ConfigConsts.Companion.OPENSTACK_IMAGE_NAME_KEY
import com.aim.server.domain.admin.const.ConfigConsts.Companion.OPENSTACK_NETWORK_NAME_KEY
import com.aim.server.domain.admin.repository.AdminConfigRepository
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
import org.openstack4j.model.network.options.PortListOptions
import org.openstack4j.openstack.OSFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class OpenStackNetworkServiceImpl(
    private val adminConfigRepository: AdminConfigRepository
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

    override fun createNetwork(
        networkName: String,
        startIpAddress: String,
        endIpAddress: String,
        gateway: String,
        cidr: String
    ): Network {
        val network = osAuthToken().networking().network().create(
            Builders.network()
                .name(networkName)
                .adminStateUp(true)
                .build()
        )
        createSubnet(network, networkName + "_subnet", startIpAddress, endIpAddress, gateway, cidr)
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
        startIpAddress: String,
        endIpAddress: String,
        gateway: String,
        cidr: String
    ): Subnet {
        val subnet = osAuthToken().networking().subnet().create(
            Builders.subnet()
                .name(subnetName)
                .networkId(network.id)
                .enableDHCP(true)
                .addPool(startIpAddress, endIpAddress)
                .ipVersion(IPVersionType.V4)
                .gateway(gateway)
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

    override fun createIpInstance(department: String, name: String, ipAddress: String): String {
        val imageId =
            adminConfigRepository.findByKey(OPENSTACK_IMAGE_NAME_KEY).getOrElse { throw Exception("Image not found") }
        val networkId = adminConfigRepository.findByKey(OPENSTACK_NETWORK_NAME_KEY)
            .getOrElse { throw Exception("Network not found") }
        val serverName = department + "_" + name
        val createServer = Builders.server()
            .name(serverName)
            .flavor(1.toString())
            .image(imageId.value)
            .addPersonality("/etc/motd", "Welcome to the AIM Cloud")
            .blockDevice(
                Builders.blockDeviceMapping().uuid(imageId.value)
                    .sourceType(BDMSourceType.IMAGE)
                    .volumeSize(1).deviceName("/dev/vda").bootIndex(0).destinationType(BDMDestType.LOCAL)
                    .deleteOnTermination(true).build()
            ).build()
        createServer.addNetwork(networkId.value, ipAddress)
        val server = osAuthToken().compute().servers().boot(createServer)
        return server.id
    }

    override fun deleteIpInstance(serverId: String) {
        osAuthToken().compute().servers().delete(serverId)
    }

    override fun updateIpInstance(serverId: String, newIpAddress: String) {
        val ports = osAuthToken().networking().port().list(PortListOptions.create().deviceId(serverId))
        val port = ports.first()
        log.info { "port: $port" }
        log.info { "ports: $ports" }
        val subnet = getSubnetList().first { it.id == port.fixedIps.first().subnetId }
        val subnetId = subnet.id
        ports.remove(port)
        port.toBuilder().fixedIp(newIpAddress, subnetId).build()
        log.info { "newPort: $port" }
        osAuthToken().networking().port().delete(port.id)
        osAuthToken().networking().port().create(port)
    }
}