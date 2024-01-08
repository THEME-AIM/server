package com.aim.server.domain.address.service

import com.aim.server.domain.address.repository.ipAddress.IpAddressRepository
import org.openstack4j.api.Builders
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.model.network.AttachInterfaceType
import org.openstack4j.model.network.IPVersionType
import org.openstack4j.model.network.Network
import org.openstack4j.model.network.Subnet
import org.springframework.beans.factory.annotation.Value

class OpenStackNetworkServiceImpl(
    private val ipAddressRepository: IpAddressRepository,
    private val osAuthToken: OSClientV3
) : OpenStackNetworkService {
    @Value("\${openstack.neutron.router.id}")
    private lateinit var routerId: String


    override fun createNetwork(networkName: String, ipAddress: String): Network {
        ipAddressRepository.findByIpAddress(ipAddress).ifPresent {
            throw IllegalArgumentException("이미 사용중인 IP 주소입니다.")
        }
        val network = osAuthToken.networking().network().create(
            Builders.network()
                .name(networkName)
                .adminStateUp(true)
                .build()
        )
        createSubnet(network, networkName + "_subnet", ipAddress)
        return network
    }

    override fun deleteNetwork(networkName: String) {
        osAuthToken.networking().network().delete(networkName)
    }

    override fun getNetork(networkName: String): Network {
        return osAuthToken.networking().network().get(networkName)
    }

    override fun getNetworkList(): List<Network> {
        return osAuthToken.networking().network().list()
    }

    private fun createSubnet(
        network: Network,
        subnetName: String,
        ipPool: String
    ): Subnet {
        val subnet = osAuthToken.networking().subnet().create(
            Builders.subnet()
                .name(subnetName)
                .networkId(network.id)
                .addPool("$ipPool.2", "$ipPool.254")
                .ipVersion(IPVersionType.V4)
                .cidr("$ipPool.0/24")
                .build()
        )
        osAuthToken.networking().router().attachInterface(routerId, AttachInterfaceType.SUBNET, subnet.id)
        return subnet
    }

    private fun deleteSubnet(subnetName: String) {
        osAuthToken.networking().subnet().delete(subnetName)
    }

    private fun getSubnet(subnetName: String): Subnet {
        return osAuthToken.networking().subnet().get(subnetName)
    }

    private fun getSubnetList(): List<Subnet> {
        return osAuthToken.networking().subnet().list()
    }
}