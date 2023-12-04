package com.aim.server.core.response

abstract class BaseResponse(private val isSuccess: Boolean, private val code: Int, private val message: String)

