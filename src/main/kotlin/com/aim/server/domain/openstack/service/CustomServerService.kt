package com.aim.server.domain.openstack.service

interface CustomServerService {
    fun createServer(projectId: Long, username: String)
    fun startServer(serverName: String)
    fun stopServer(serverName: String)
    fun deleteServer(serverName: String)
}