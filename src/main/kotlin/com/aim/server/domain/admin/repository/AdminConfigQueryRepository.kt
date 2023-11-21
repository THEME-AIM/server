package com.aim.server.domain.admin.repository

import java.util.*

interface AdminConfigQueryRepository {
    fun findValueByKey(key: String): Optional<String>
}