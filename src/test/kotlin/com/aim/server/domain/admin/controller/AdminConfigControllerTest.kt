package com.aim.server.domain.admin.controller

import AbstractControllerTest
import com.aim.server.domain.admin.dto.AdminConfigData
import com.aim.server.domain.admin.dto.AdminConfigData.AdminKeys
import docs.makeDocument
import docs.request.request
import docs.request.requestBody
import docs.response.baseResponseBody
import docs.response.response
import docs.type.*
import io.restassured.http.Method
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.restdocs.RestDocumentationContextProvider


class AdminConfigControllerTest(
    @LocalServerPort port: Int
) : AbstractControllerTest(port) {
    private var sessionId: String = ""

    @BeforeEach
    override fun setUp(restDocumentation: RestDocumentationContextProvider?) {
        super.setUp(restDocumentation)

        // * DEFAULT GIVEN: 로그인 완료된 관리자 세션 ID
        sessionId = getSessionId()
    }

    @DisplayName("관리자 설정 목록 조회: 정상(조회 완료)")
    @Test
    fun getAdminConfigListWith200() {
        // * GIVEN: 테스트용 관리자 설정 목록
        // * DB 내부에 관리자용 ID 와 PW 데이터가 이미 저장 완료되어있음.

        // * WHEN: 관리자 설정 목록 조회 API 호출
        // * THEN: 200 OK: 관리자 설정 목록 조회 완료
        this.spec.makeDocument(identifier = "admin/config/get/200") {
            url = "/api/admin/config"
            method = Method.GET
            statusCode = HttpStatus.OK
            session = sessionId
            response {
                responseBody = baseResponseBody(
                    "data[].id" type NUMBER means "관리자 설정 ID 값",
                    "data[].key" type STRING means "관리자 설정 키 값",
                    "data[].value" type STRING means "관리자 설정 값",
                )
            }
        }
    }

    @DisplayName("관리자 설정 생성/수정: 정상 생성 완료")
    @Test
    fun upsertAdminConfigWith200() {
        // * GIVEN: 테스트용 관리자 설정 생성/수정 데이터
        val keyAPIRequest = AdminConfigData.KeyAPIRequest(
            keys = listOf(
                AdminKeys(
                    key = "admin.startIpAddress",
                    value = "192.168.0.1"
                ),
                AdminKeys(
                    key = "admin.endIpAddress",
                    value = "192.168.0.100"
                )
            )
        ).toJson()

        // * WHEN: 관리자 설정 생성/수정 API 호출
        // * THEN: 200 OK: 관리자 설정 생성/수정 완료
        this.spec.makeDocument(identifier = "admin/config/upsert/200") {
            url = "/api/admin/config"
            method = Method.PATCH
            statusCode = HttpStatus.OK
            session = sessionId
            requestBodyValue = keyAPIRequest
            request {
                requestBody = requestBody(
                    "keys[]" type ARRAY means "관리자 설정 키 배열",
                    "keys[].key" type STRING means "관리자 설정 키 값",
                    "keys[].value" type STRING means "관리자 설정 값",
                )
            }
            response {
                responseBody = baseResponseBody(
                    "data" type LIST means "생성/수정된 관리자 설정 목록",
                    "data[].id" type NUMBER means "관리자 설정 ID 값",
                    "data[].key" type STRING means "관리자 설정 키 값",
                    "data[].value" type STRING means "관리자 설정 값",
                )
            }
        }
    }
}