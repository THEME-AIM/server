//package com.aim.server.domain.admin.controller
//
//import AbstractControllerTest
//import com.aim.server.domain.admin.dto.AdminConfigData.*
//import docs.makeDocument
//import docs.request.request
//import docs.request.requestBody
//import docs.response.baseResponseBody
//import docs.response.errorResponseBody
//import docs.response.response
//import docs.type.*
//import io.restassured.http.Method
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.springframework.boot.test.web.server.LocalServerPort
//import org.springframework.http.HttpStatus
//import org.springframework.restdocs.RestDocumentationContextProvider
//
//
//class AdminConfigControllerTest(
//    @LocalServerPort port: Int
//) : AbstractControllerTest(port) {
//    private var sessionId: String = ""
//
//    @BeforeEach
//    override fun setUp(restDocumentation: RestDocumentationContextProvider?) {
//        super.setUp(restDocumentation)
//
//        // * DEFAULT GIVEN: 로그인 완료된 관리자 세션 ID
//        sessionId = getSessionId()
//    }
//
//    @DisplayName("관리자 설정 목록 조회: 정상(조회 완료)")
//    @Test
//    fun getAdminConfigListWith200() {
//        // * GIVEN: 테스트용 관리자 설정 목록
//        // * DB 내부에 관리자용 ID 와 PW 데이터가 이미 저장 완료되어있음.
//
//        // * WHEN: 관리자 설정 목록 조회 API 호출
//        // * THEN: 200 OK: 관리자 설정 목록 조회 완료
//        this.spec.makeDocument(identifier = "admin/config-list/200") {
//            url = "/api/admin/config"
//            method = Method.GET
//            statusCode = HttpStatus.OK
//            session = sessionId
//            response {
//                responseBody = baseResponseBody(
//                    "data[].id" type NUMBER means "관리자 설정 ID 값",
//                    "data[].key" type STRING means "관리자 설정 키 값",
//                    "data[].value" type STRING means "관리자 설정 값",
//                )
//            }
//        }
//    }
//
//    @DisplayName("관리자 설정 생성/수정: 정상 생성 완료")
//    @Test
//    fun upsertAdminConfigWith200() {
//        // * GIVEN: 테스트용 관리자 설정 생성/수정 데이터
//        val keyAPIRequest = KeyAPIRequest(
//            keys = listOf(
//                AdminKeys(
//                    key = "admin_start_ip_ddress",
//                    value = "192.168.0.1"
//                ),
//                AdminKeys(
//                    key = "admin_end_ip_ddress",
//                    value = "192.168.0.100"
//                ),
//                AdminKeys(
//                    key = "admin_gateway_ip_address",
//                    value = "192.168.0.255"
//                ),
//                AdminKeys(
//                    key = "admin_subnet_mask_key",
//                    value = "24"
//                ),
//                AdminKeys(
//                    key = "admin_default_dns_address",
//                    value = "8.8.8.8"
//                ),
//            )
//        ).toJson()
//
//        // * WHEN: 관리자 설정 생성/수정 API 호출
//        // * THEN: 200 OK: 관리자 설정 생성/수정 완료
//        this.spec.makeDocument(identifier = "admin/config-update/200") {
//            url = "/api/admin/config"
//            method = Method.PATCH
//            statusCode = HttpStatus.OK
//            session = sessionId
//            requestBodyValue = keyAPIRequest
//            request {
//                requestBody = requestBody(
//                    "keys[]" type ARRAY means "관리자 설정 키 배열",
//                    "keys[].key" type STRING means "관리자 설정 키 값",
//                    "keys[].value" type STRING means "관리자 설정 값",
//                )
//            }
//            response {
//                responseBody = baseResponseBody(
//                    "data" type LIST means "생성/수정된 관리자 설정 목록",
//                    "data[].id" type NUMBER means "관리자 설정 ID 값",
//                    "data[].key" type STRING means "관리자 설정 키 값",
//                    "data[].value" type STRING means "관리자 설정 값",
//                )
//            }
//        }
//    }
//
//    @DisplayName("관리자 설정 생성/수정: 잘못된 키 값")
//    @Test
//    fun upsertAdminConfigWith400() {
//        // * GIVEN: 테스트용 관리자 설정 생성/수정 데이터
//        val keyAPIRequest = KeyAPIRequest(
//            keys = listOf(
//                AdminKeys(
//                    key = "subnet_mask_key",
//                    value = "192.168.0.1",
//                ),
//            )
//        ).toJson()
//
//        // * WHEN: 관리자 설정 생성/수정 API 호출
//        // * THEN: 400 BAD_REQUEST: 잘못된 키 값
//        this.spec.makeDocument(identifier = "admin/config-update/400") {
//            url = "/api/admin/config"
//            method = Method.PATCH
//            statusCode = HttpStatus.BAD_REQUEST
//            session = sessionId
//            requestBodyValue = keyAPIRequest
//            request {
//                requestBody = requestBody(
//                    "keys[]" type ARRAY means "관리자 설정 키 배열",
//                    "keys[].key" type STRING means "관리자 설정 키 값",
//                    "keys[].value" type STRING means "관리자 설정 값",
//                )
//            }
//            response {
//                responseBody = errorResponseBody()
//            }
//        }
//    }
//
//    @DisplayName("층별 시작/종료 IP 주소 설정: 정상 설정 완료")
//    @Test
//    fun upsertAdminConfigFloorWith200() {
//        // * GIVEN: 테스트용 층별 시작/종료 IP 주소 설정 데이터
//        val floorAPIRequest = FloorAPIRequest(
//            floors = listOf(
//                FloorKeys(
//                    floor = 1,
//                    startIpAddress = "192.168.0.1",
//                    endIpAddress = "192.168.0.10"
//                ),
//                FloorKeys(
//                    floor = 2,
//                    startIpAddress = "192.168.0.11",
//                    endIpAddress = "192.168.0.20"
//                )
//            )
//        ).toJson()
//
//        // * WHEN: 층별 시작/종료 IP 주소 설정 API 호출
//        // * THEN: 200 OK: 층별 시작/종료 IP 주소 설정 완료
//        this.spec.makeDocument(identifier = "admin/floor/200") {
//            url = "/api/admin/floor"
//            method = Method.POST
//            statusCode = HttpStatus.OK
//            session = sessionId
//            requestBodyValue = floorAPIRequest
//            request {
//                requestBody = requestBody(
//                    "floors[]" type ARRAY means "층별 시작/종료 IP 주소 설정 배열",
//                    "floors[].floor" type NUMBER means "층",
//                    "floors[].startIpAddress" type STRING means "시작 IP 주소",
//                    "floors[].endIpAddress" type STRING means "종료 IP 주소",
//                )
//            }
//            response {
//                responseBody = baseResponseBody(
//                    "data" type LIST means "생성/수정된 층별 시작/종료 IP 주소 설정 목록",
//                    "data[].id" type NUMBER means "관리자 설정 ID 값",
//                    "data[].key" type STRING means "관리자 설정 키 값",
//                    "data[].value" type STRING means "관리자 설정 값",
//                )
//            }
//        }
//    }
//
//    @DisplayName("층별 시작/종료 IP 주소 설정: 잘못된 층 값")
//    @Test
//    fun upsertAdminConfigFloorWith400ByFloor() {
//        // * GIVEN: 테스트용 층별 시작/종료 IP 주소 설정 데이터
//        val request = FloorAPIRequest(
//            floors = listOf(
//                FloorKeys(
//                    floor = 0,
//                    startIpAddress = "192.168.0.1",
//                    endIpAddress = "192.168.0.10",
//                )
//            )
//        ).toJson()
//
//        // * WHEN: 층별 시작/종료 IP 주소 설정 API 호출
//        // * THEN: 400 BAD_REQUEST: 잘못된 층 값
//        this.spec.makeDocument(identifier = "admin/floor/400/floor-error") {
//            url = "/api/admin/floor"
//            method = Method.POST
//            statusCode = HttpStatus.BAD_REQUEST
//            session = sessionId
//            requestBodyValue = request
//            request {
//                requestBody = requestBody(
//                    "floors[]" type ARRAY means "층별 시작/종료 IP 주소 설정 배열",
//                    "floors[].floor" type NUMBER means "층",
//                    "floors[].startIpAddress" type STRING means "시작 IP 주소",
//                    "floors[].endIpAddress" type STRING means "종료 IP 주소",
//                )
//            }
//            response {
//                responseBody = errorResponseBody()
//            }
//        }
//    }
//
//    @DisplayName("층별 시작/종료 IP 주소 설정: 빈 값 전송")
//    @Test
//    fun upsertAdminConfigFloorWith400ByEmptyValue() {
//        // * GIVEN: 테스트용 층별 시작/종료 IP 주소 설정 데이터
//        val request = FloorAPIRequest(
//            floors = listOf(
//                FloorKeys(
//                    floor = 1,
//                    startIpAddress = "",
//                    endIpAddress = "",
//                )
//            )
//        ).toJson()
//
//        // * WHEN: 층별 시작/종료 IP 주소 설정 API 호출
//        // * THEN: 400 BAD_REQUEST: 빈 값 전송
//        this.spec.makeDocument(identifier = "admin/floor/400/empty-value") {
//            url = "/api/admin/floor"
//            method = Method.POST
//            statusCode = HttpStatus.BAD_REQUEST
//            session = sessionId
//            requestBodyValue = request
//            request {
//                requestBody = requestBody(
//                    "floors[]" type ARRAY means "층별 시작/종료 IP 주소 설정 배열",
//                    "floors[].floor" type NUMBER means "층",
//                    "floors[].startIpAddress" type STRING means "시작 IP 주소",
//                    "floors[].endIpAddress" type STRING means "종료 IP 주소",
//                )
//            }
//            response {
//                responseBody = errorResponseBody()
//            }
//        }
//    }
//
//    @DisplayName("층별 시작/종료 IP 주소 설정: 잘못된 IP 주소 할당")
//    @Test
//    fun upsertAdminConfigFloorWith400ByIp() {
//        // * GIVEN: 테스트용 층별 시작/종료 IP 주소 설정 데이터
//        val request = FloorAPIRequest(
//            floors = listOf(
//                FloorKeys(
//                    floor = 1,
//                    startIpAddress = "192.168.0.1",
//                    endIpAddress = "192.168.0",
//                )
//            )
//        ).toJson()
//
//        // * WHEN: 층별 시작/종료 IP 주소 설정 API 호출
//        // * THEN: 400 BAD_REQUEST: 잘못된 IP 주소 할당
//        this.spec.makeDocument(identifier = "admin/floor/400/ip-address-error") {
//            url = "/api/admin/floor"
//            method = Method.POST
//            statusCode = HttpStatus.BAD_REQUEST
//            session = sessionId
//            requestBodyValue = request
//            request {
//                requestBody = requestBody(
//                    "floors[]" type ARRAY means "층별 시작/종료 IP 주소 설정 배열",
//                    "floors[].floor" type NUMBER means "층",
//                    "floors[].startIpAddress" type STRING means "시작 IP 주소",
//                    "floors[].endIpAddress" type STRING means "종료 IP 주소",
//                )
//            }
//            response {
//                responseBody = errorResponseBody()
//            }
//        }
//    }
//}