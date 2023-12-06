package com.aim.server.domain.admin.controller

import com.aim.server.domain.admin.const.ConfigConsts.Companion.DEFAULT_ADMIN_PASSWORD_VALUE
import com.aim.server.domain.admin.const.ConfigConsts.Companion.DEFAULT_ADMIN_USERNAME_VALUE
import com.aim.server.domain.admin.dto.AdminConfigData.*
import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AdminConfigControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("관리자 로그인 페이지")
    fun signInOk() {
        val signInRequest = SignInRequest(
            DEFAULT_ADMIN_USERNAME_VALUE,
            DEFAULT_ADMIN_PASSWORD_VALUE
        )
        mockMvc.perform(
            post("/api/admin/sign-in")
                .contentType("application/json")
                .content(Gson().toJson(signInRequest))
        ).andExpect(
            status().isOk
        ).andDo(
            document(
                "admin/sign-in",
                requestFields(
                    fieldWithPath("username").description("관리자 아이디"),
                    fieldWithPath("password").description("관리자 비밀번호")
                ),
                responseFields(
                    fieldWithPath("isSuccess").description("성공여부"),
                    fieldWithPath("message").description("메시지"),
                    fieldWithPath("code").description("코드"),
                    fieldWithPath("data").description("데이터"),
                )
            )
        )
    }

    @Test
    @DisplayName("관리자 로그인 페이지")
    fun signIn400() {
        val signInRequest = SignInRequest(
            DEFAULT_ADMIN_USERNAME_VALUE,
            "wrong password"
        )
        mockMvc.perform(
            post("/api/admin/sign-in")
                .contentType("application/json")
                .content(Gson().toJson(signInRequest))
        ).andExpect {
            assertEquals(HttpStatus.BAD_REQUEST.value(), it.response.status)
        }.andDo(
            document(
                "admin/sign-in",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("username").type(JsonFieldType.STRING).description("관리자 아이디"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("관리자 비밀번호")
                ),
                responseFields(
                    fieldWithPath("isSuccess").description("성공여부"),
                    fieldWithPath("message").description("메시지"),
                    fieldWithPath("code").description("코드"),
                    fieldWithPath("errors").description("데이터"),
                )
            )
        )
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