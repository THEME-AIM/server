package com.aim.server.core.response

data class FieldErrorResponse(
    val field: String,
    val value: String,
    val code: String,
    val message: String,
)
