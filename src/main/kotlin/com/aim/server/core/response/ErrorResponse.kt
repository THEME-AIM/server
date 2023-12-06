package com.aim.server.core.response

import com.aim.server.core.exception.ErrorCode
import org.springframework.validation.Errors


data class ErrorResponse(
    val isSuccess: Boolean = false,
    val code: Int,
    val message: String,
    val errors: List<FieldErrorResponse> = emptyList(),
) : BaseResponse<Unit>(
    isSuccess = isSuccess,
    code = code,
    message = message,
    data = null,
    errors = errors,
) {
    constructor(errorCode: ErrorCode) : this(code = errorCode.httpStatus.value(), message = errorCode.errorMessage)
    constructor(errorCode: ErrorCode, message: String) : this(code = errorCode.httpStatus.value(), message = message)
    constructor(errorCode: ErrorCode, errors: List<FieldErrorResponse>) : this(
        code = errorCode.httpStatus.value(),
        message = errorCode.errorMessage,
        errors = errors
    )

    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(errorCode = errorCode)
        }

        fun of(errorCode: ErrorCode, errors: Errors): ErrorResponse {
            return ErrorResponse(errorCode = errorCode, errors = errors.toFieldErrorResponse())
        }

        fun of(errorCode: ErrorCode, message: String): ErrorResponse {
            return ErrorResponse(errorCode = errorCode, message = message)
        }

        private fun Errors.toFieldErrorResponse(): List<FieldErrorResponse> =
            this.fieldErrors.map {
                FieldErrorResponse(
                    field = it.field,
                    value = it.rejectedValue.toString(),
                    code = it.code.toString(),
                    message = it.defaultMessage.toString()
                )
            }
    }
}