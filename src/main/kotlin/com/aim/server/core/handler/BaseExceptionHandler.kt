package com.aim.server.core.handler

import com.aim.server.core.exception.BaseException
import com.aim.server.core.exception.ErrorCode
import com.aim.server.core.response.BaseResponse
import com.aim.server.core.response.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice(basePackages = ["com.aim.server"])
class BaseExceptionHandler : ResponseEntityExceptionHandler() {
    private val log = KotlinLogging.logger { }

    @ExceptionHandler(value = [Exception::class])
    @ResponseBody
    fun handleException(exception: Exception): ResponseEntity<BaseResponse> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.of(ErrorCode.SERVER_ERROR))
    }

    @ExceptionHandler(value = [BaseException::class])
    @ResponseBody
    fun handleBaseException(baseException: BaseException): ResponseEntity<BaseResponse> {
        return ResponseEntity
            .status(baseException.httpStatus)
            .body(ErrorResponse.of(baseException.errorCode))
    }
}