package com.aim.server.domain.admin.repository

import com.aim.server.domain.admin.entity.AdminConfig
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
interface AdminConfigRepository : JpaRepository<AdminConfig, Long>, AdminConfigQueryRepository {

    fun findByKey(key: String): Optional<AdminConfig>
}