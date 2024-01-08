package com.aim.server.domain.openstack.repository

import com.aim.server.domain.openstack.entity.CustomProject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomProjectRepository : JpaRepository<CustomProject, Long> {
    fun findCustomProjectByName(name: String): Optional<CustomProject>
    fun findCustomProjectById(id: Long): Optional<CustomProject>
}