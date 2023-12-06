package com.aim.server.core.response

import com.aim.server.core.exception.ErrorCode


data class ErrorResponse(
    val isSuccess: Boolean = false,
    val code: Int,
    val message: String,
    val errors: Any? = null
) : BaseResponse<Unit>(
    isSuccess = isSuccess,
    code = code,
    message = message,
    data = null,
    errors = errors,
) {
    constructor(errorCode: ErrorCode) : this(code = errorCode.httpStatus.value(), message = errorCode.errorMessage)
    constructor(errorCode: ErrorCode, message: String) : this(code = errorCode.httpStatus.value(), message = message)
    constructor(errorCode: ErrorCode, message: String, errors: Any?) : this(
        code = errorCode.httpStatus.value(),
        message = message,
        errors = errors
    )

    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(errorCode = errorCode)
        }

        fun of(errorCode: ErrorCode, message: String): ErrorResponse {
            return ErrorResponse(errorCode = errorCode, message = message)
        }
    }
}