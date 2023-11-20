package com.aim.server.domain.admin.repository

import com.aim.server.domain.admin.entity.AdminConfig
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
interface AdminConfigRepository: JpaRepository<AdminConfig, Long> {
    fun findByKey(key: String): Optional<AdminConfig>
}