package com.aim.server.domain.admin.controller

import base.AbstractControllerTest
import com.aim.server.domain.admin.const.ConfigConsts.Companion.DEFAULT_ADMIN_PASSWORD_VALUE
import com.aim.server.domain.admin.const.ConfigConsts.Companion.DEFAULT_ADMIN_USERNAME_VALUE
import com.aim.server.domain.admin.dto.AdminConfigData.*
import docs.*
import io.restassured.http.Method
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.*


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension::class)
class AdminConfigControllerTest(
    @LocalServerPort port: Int
) : AbstractControllerTest(port) {
    @DisplayName("관리자 로그인 처리 200 응답")
    @Test
    fun signInWith200() {
        this.spec.makeDocument(identifier = "signIn-200") {
            url = "/api/admin/sign-in"
            method = Method.POST
            statusCode = HttpStatus.OK
            requestBodyValue = SignInRequest(
                DEFAULT_ADMIN_USERNAME_VALUE,
                DEFAULT_ADMIN_PASSWORD_VALUE
            ).toJson()
            request {
                requestBody = requestBody(
                    "username" type STRING means "관리자 아이디" require true,
                    "password" type STRING means "관리자 비밀번호" require true,
                )
            }
            response {
                responseBody = responseBody(
                    "isSuccess" type BOOLEAN means "성공여부",
                    "message" type STRING means "메시지",
                    "code" type NUMBER means "코드",
                    "data" type OBJECT means "데이터",
                )
            }
        }
    }

    @Test
    fun signOut() {
    }

    @Test
    fun getAdminConfigs() {
    }

    @Test
    fun upsertAdminConfigs() {
    }

    @Test
    fun upsertFloors() {
    }
}