package com.aim.server.domain.admin.service

import com.aim.server.domain.admin.dto.AdminConfigData.*
import org.springframework.validation.Errors

interface AdminConfigService {
    fun getAdminConfigs(): List<APIResponse>
    fun getAdminConfig(key: String): String
    fun signIn(signIn: SignInRequest, errors: Errors)
    fun upsertAdminConfigs(configs: List<AdminKeys>): List<APIResponse>
    fun upsertFloorConfigs(configs: List<FloorKeys>): List<APIResponse>
    fun createIpAddressConfig(config: IpAddressRequest): List<APIResponse>
}