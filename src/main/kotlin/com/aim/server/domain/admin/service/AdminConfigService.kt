package com.aim.server.domain.admin.service

import com.aim.server.domain.admin.dto.AdminConfigData.*

interface AdminConfigService {
    fun getAdminConfigs(): List<APIResponse>
    fun getAdminConfig(key: String): String
    fun signIn(signIn: SignInRequest)
    fun upsertAdminConfigs(configs: List<APIRequest>): List<APIResponse>
}