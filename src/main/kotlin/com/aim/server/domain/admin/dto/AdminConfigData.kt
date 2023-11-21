package com.aim.server.domain.admin.dto

class AdminConfigData {
    data class Response(
        val id: Long,
        val key: String,
        val value: String
    )

    data class Request(
        val key: String,
        val value: String
    )

    data class SignInRequest(
        val username: String,
        val password: String
    )
}