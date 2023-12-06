package com.aim.server.domain.admin.dto

class AdminConfigData {
    data class APIResponse(
        val id: Long,
        val key: String,
        val value: String
    )

    data class APIRequest(
        val key: String,
        val value: String
    )

    data class SignInRequest(
        val username: String,
        val password: String
    )
}