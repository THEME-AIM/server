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
        @field: Range(min = 1, max = 10)
        val floor: Int,

        @field: NotBlank
        @field: IPv4
        val startIpAddress: String,

        @field: NotBlank
        @field: IPv4
        val endIpAddress: String,
    ) {
        companion object {
            fun betweenIpAddress(startIpAddress: String, endIpAddress: String): Set<String> {
                val startIp = startIpAddress.split(".").map { it.toInt() }
                val endIp = endIpAddress.split(".").map { it.toInt() }
                val ipAddresses = mutableListOf<String>()
                for (i in startIp[0]..endIp[0]) {
                    for (j in startIp[1]..endIp[1]) {
                        for (k in startIp[2]..endIp[2]) {
                            for (l in startIp[3]..endIp[3]) {
                                ipAddresses.add("$i.$j.$k.$l")
                            }
                        }
                    }
                }
                return ipAddresses.toSet()
            }
        }
    }

    data class SignInRequest(
        @field: NotBlank
        val username: String,
        
        @field: NotBlank
        val password: String
    )
}