package com.aim.server.domain.openstack.repository

import com.aim.server.domain.openstack.entity.CustomServer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomServerRepository : JpaRepository<CustomServer, Long> {
    fun findCustomServerByServerName(name: String): Optional<CustomServer>
}