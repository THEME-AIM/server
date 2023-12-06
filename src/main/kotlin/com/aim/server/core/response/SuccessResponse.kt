package com.aim.server.core.response

import org.springframework.http.HttpStatus

data class SuccessResponse<T>(
    val isSuccess: Boolean = true,
    val code: Int,
    val message: String,
    val data: T,
) : BaseResponse<T>(
    isSuccess = isSuccess,
    code = code,
    message = message,
    data = data,
    errors = null,
) {
    constructor(code: Int, message: String, data: T) : this(true, code, message, data)
    constructor(message: String, data: T) : this(true, HttpStatus.OK.value(), message, data)
    constructor(data: T) : this(true, HttpStatus.OK.value(), "OK", data)

    companion object {
        fun <T> of(code: Int, message: String, data: T): SuccessResponse<T> {
            return SuccessResponse(code, message, data)
        }

        fun <T> of(data: T): SuccessResponse<T> {
            return SuccessResponse(data)
        }

        fun <T> of(message: String, data: T): SuccessResponse<T> {
            return SuccessResponse(message, data)
        }

        fun empty(): SuccessResponse<Unit> {
            return SuccessResponse(Unit)
        }
    }
}