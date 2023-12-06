package com.aim.server.domain.admin.dto

import com.aim.server.domain.admin.annotation.AdminKey
import com.aim.server.domain.admin.annotation.IPv4
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.Range

class AdminConfigData {
    data class APIResponse(
        val id: Long,
        val key: String,
        val value: String
    )

    data class KeyAPIRequest(
        @field: NotEmpty
        @field: Valid
        val keys: List<AdminKeys>
    )

    data class AdminKeys(
        @field: NotBlank
        @field: AdminKey
        val key: String,

        @field:NotBlank
        val value: String
    )

    data class FloorAPIRequest(
        @field: NotEmpty
        @field: Valid
        val floors: List<FloorKeys>
    )

    data class FloorKeys(
        @field: NotBlank
        @field: Range(min = 1, max = 10)
        val floor: Int,

        @field: NotBlank
        @field: AdminKey
        @field: IPv4
        val startIpAddress: String,

        @field: NotBlank
        @field: AdminKey
        @field: IPv4
        val endIpAddress: String
    )

    data class SignInRequest(
        val username: String,
        val password: String
    )
}