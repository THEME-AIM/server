package com.aim.server.domain.address.service

import org.openstack4j.model.network.Network

interface OpenStackNetworkService {
    fun createNetwork(networkName: String, ipAddress: String): Network
    fun deleteNetwork(networkName: String)
    fun getNetork(networkName: String): Network
    fun getNetworkList(): List<Network>
    fun createServer1(projectId: Long, username: String)
    fun createServer2(projectId: Long, username: String)
}