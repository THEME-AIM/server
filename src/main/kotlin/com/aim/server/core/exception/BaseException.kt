package com.aim.server.core.exception

class BaseException(
    val errorCode: ErrorCode
) : RuntimeException() {
    constructor(errorCode: ErrorCode, message: String) : this(errorCode) {
        this.message = message
    }

    constructor(errorCode: ErrorCode, message: String, cause: Throwable) : this(errorCode) {
        this.message = message
        this.cause = cause
    }

    constructor(errorCode: ErrorCode, cause: Throwable) : this(errorCode) {
        this.cause = cause
    }

    val httpStatus: Int = errorCode.httpStatus.value()
    override var message: String? = errorCode.errorMessage
    override var cause: Throwable? = null
}