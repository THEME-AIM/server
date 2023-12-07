package com.aim.server.domain.admin.controller

import AbstractControllerTest
import com.aim.server.domain.admin.const.ConfigConsts.Companion.DEFAULT_ADMIN_PASSWORD_VALUE
import com.aim.server.domain.admin.const.ConfigConsts.Companion.DEFAULT_ADMIN_USERNAME_VALUE
import com.aim.server.domain.admin.dto.AdminConfigData.*
import docs.*
import docs.request.request
import docs.request.requestBody
import docs.response.baseResponseBody
import docs.response.response
import docs.type.*
import io.restassured.http.Method
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.*


class AuthControllerTest(
    @LocalServerPort port: Int
) : AbstractControllerTest(port) {

    @DisplayName("관리자 로그인: 정상(로그인 완료)")
    @Test
    fun signInWith200() {
        // * GIVEN: 정상적인 관리자 로그인 아이디
        val body = SignInRequest(
            DEFAULT_ADMIN_USERNAME_VALUE,
            DEFAULT_ADMIN_PASSWORD_VALUE
        ).toJson()

        // * WHEN: 관리자 로그인 API 호출
        // * THEN: 200 OK: 정상로그인 완료
        this.spec.makeDocument(identifier = "admin/sign-in/200") {
            url = "/api/admin/sign-in"
            method = Method.POST
            statusCode = HttpStatus.OK
            requestBodyValue = body
            request {
                requestBody = requestBody(
                    "username" type STRING means "관리자 아이디" require true,
                    "password" type STRING means "관리자 비밀번호" require true,
                )
            }
            response {
                headers = responseHeaders(
                    "Set-Cookie" meanHeader "로그인 세션 ID"
                )
                responseBody = baseResponseBody(
                    "data" type OBJECT means "데이터",
                )
            }
        }
    }

    @DisplayName("관리자 로그인: 비밀번호 오류")
    @Test
    fun signInWith400() {
        // * GIVEN: 잘못된 비밀번호 입력
        val body = SignInRequest(
            DEFAULT_ADMIN_USERNAME_VALUE,
            "WRONG_PASSWORD"
        ).toJson()

        // * WHEN: 관리자 로그인 API 호출
        // * THEN: 400 BAD_REQUEST: 로그인 에러
        this.spec.makeDocument(identifier = "admin/sign-in/400") {
            url = "/api/admin/sign-in"
            method = Method.POST
            statusCode = HttpStatus.BAD_REQUEST
            requestBodyValue = body
            request {
                requestBody = requestBody(
                    "username" type STRING means "관리자 아이디" require true,
                    "password" type STRING means "관리자 비밀번호" require true,
                )
            }
            response {
                responseBody = baseResponseBody(
                    "errors" type LIST means "오류 목록",
                )
            }
        }
    }

    @DisplayName("관리자 로그인: 세션 오류(재로그인 시)")
    @Test
    fun signInWith409() {
        // * GIVEN: 정상적인 관리자 로그인 아이디 와 로그인된 세션 ID
        val body = SignInRequest(
            DEFAULT_ADMIN_USERNAME_VALUE,
            DEFAULT_ADMIN_PASSWORD_VALUE
        ).toJson()
        val sessionId = getSessionId()

        // * WHEN: 이미 로그인을 완료한 상태에서 재 로그인 시도
        // * THEN: 409 CONFLICT: 로그인 에러
        this.spec.makeDocument(identifier = "admin/sign-in/409") {
            url = "/api/admin/sign-in"
            method = Method.POST
            statusCode = HttpStatus.CONFLICT
            session = sessionId
            requestBodyValue = body
            request {
                requestBody = requestBody(
                    "username" type STRING means "관리자 아이디" require true,
                    "password" type STRING means "관리자 비밀번호" require true,
                )
            }
            response {
                responseBody = baseResponseBody(
                    "errors" type LIST means "성공 결과",
                )
            }
        }
    }

    @DisplayName("관리자 로그아웃: 정상(로그아웃 완료)")
    @Test
    fun signOutWith200() {
        // * GIVEN: 로그인된 세션 ID
        val sessionId = getSessionId()
        print(sessionId)
        // * WHEN: 관리자 로그인 API 호출
        // * THEN: 200 OK: 정상로그인 완료
        this.spec.makeDocument(identifier = "admin/sign-out/200") {
            url = "/api/admin/sign-out"
            method = Method.DELETE
            statusCode = HttpStatus.NO_CONTENT
            session = sessionId
        }
    }

    @DisplayName("관리자 로그아웃: 세션 오류(로그인 안된 상태)")
    @Test
    fun signOutWith401() {
        // * GIVEN: 로그인 되지 않은 상태.
        // * WHEN: 관리자 로그인 API 호출
        // * THEN: 401 UNAUTHORIZED: 로그인 안된 상태에서 로그아웃 시도
        this.spec.makeDocument(identifier = "admin/sign-out/401") {
            url = "/api/admin/sign-out"
            method = Method.DELETE
            statusCode = HttpStatus.UNAUTHORIZED
            response {
                responseBody = baseResponseBody(
                    "errors" type LIST means "오류 목록",
                )
            }
        }
    }
}