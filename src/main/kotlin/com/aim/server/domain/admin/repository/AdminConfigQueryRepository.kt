package com.aim.server.domain.admin.repository

import com.aim.server.domain.admin.entity.AdminConfig
import java.util.*

interface AdminConfigQueryRepository {
    fun findValueByKey(key: String): Optional<String>
    fun findAdminConfigByKeys(keys: List<String>): List<AdminConfig>
}