package com.aim.server.domain.address.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.api.exceptions.AuthenticationException
import org.openstack4j.model.common.Identifier
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

    @Value("\${openstack.neutron.router.id}")
    private lateinit var routerId: String


    override fun createNetwork(networkName: String, ipAddress: String): Network {
//        ipAddressRepository.findByIpAddress(ipAddress).ifPresent {
//            throw IllegalArgumentException("이미 사용중인 IP 주소입니다.")
//        }
        println("test networkName: $networkName")
        println(osAuthToken())
        println(osAuthToken().networking().network().list())
        val network = osAuthToken().networking().network().create(
            Builders.network()
                .name(networkName)
                .adminStateUp(true)
                .build()
        )
        println("test network: $network")
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
        ipPool: String
    ): Subnet {
        val subnet = osAuthToken().networking().subnet().create(
            Builders.subnet()
                .name(subnetName)
                .networkId(network.id)
                .enableDHCP(true)
                .addPool("$ipPool.2", "$ipPool.254")
                .ipVersion(IPVersionType.V4)
                .cidr("$ipPool.0/24")
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
}