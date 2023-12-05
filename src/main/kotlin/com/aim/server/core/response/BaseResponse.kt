package com.aim.server.core.response

abstract class BaseResponse<T>(
    private val isSuccess: Boolean,
    private val code: Int,
    private val message: String,
    private val data: T?,
    private val errors: Any?,
)

