package com.aim.server.domain.address.service

import org.openstack4j.model.network.Network

interface OpenStackNetworkService {
    fun createNetwork(
        networkName: String,
        startIpAddress: String,
        endIpAddress: String,
        gateway: String,
        cidr: String
    ): Network

    fun deleteNetwork(networkName: String)
    fun getNetork(networkName: String): Network
    fun getNetworkList(): List<Network>
    fun createIpInstance(department: String, name: String, ipAddress: String): String
    fun deleteIpInstance(serverId: String)
    fun updateIpInstance(serverId: String, newIpAddress: String)
}