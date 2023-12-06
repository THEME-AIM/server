package com.aim.server.core.handler

import com.aim.server.core.exception.BaseException
import com.aim.server.core.exception.ErrorCode
import com.aim.server.core.response.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice(basePackages = ["com.aim.server"])
class BaseExceptionHandler : ResponseEntityExceptionHandler() {
    private val log = KotlinLogging.logger { }

    @ExceptionHandler(value = [Exception::class])
    fun handleException(exception: Exception): ResponseEntity<ErrorResponse> {
        exception.printStackTrace()
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.of(ErrorCode.SERVER_ERROR))
    }

    @ExceptionHandler(value = [BaseException::class])
    fun handleBaseException(baseException: BaseException): ResponseEntity<ErrorResponse> {
        return if (baseException.errors != null) {
            ResponseEntity
                .status(baseException.errorCode.httpStatus)
                .body(ErrorResponse.of(errorCode = baseException.errorCode, errors = baseException.errors))
        } else {
            ResponseEntity
                .status(baseException.errorCode.httpStatus)
                .body(ErrorResponse.of(errorCode = baseException.errorCode))
        }

    }
}