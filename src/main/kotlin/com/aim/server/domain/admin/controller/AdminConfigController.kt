package com.aim.server.domain.admin.controller

import com.aim.server.core.annotation.IsAuthenticated
import com.aim.server.domain.admin.const.ConfigConsts.Companion.LOGIN_SESSION
import com.aim.server.domain.admin.dto.AdminConfigData.*
import com.aim.server.domain.admin.service.AdminConfigService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/admin"])
class AdminConfigController(
    private val adminConfigService: AdminConfigService
) {

    /**
     * 관리자 로그인
     * @param signIn: SignInRequest: 관리자 로그인 정보 (ID / Password)
     */
    @PostMapping(value = ["/sign-in"])
    @ResponseStatus(value = HttpStatus.OK)
    fun signIn(
        request: HttpServletRequest,
        @RequestBody signIn: SignInRequest,
    ) {
        // session을 조회하여 이미 로그인 되어있는지 확인
        val session = request.session.getAttribute(LOGIN_SESSION)
        if (session != null && session as Boolean) {
            throw Exception("Already logged in")
        }

        // 로그인 정보를 통해 관리자 로그인
        adminConfigService.signIn(signIn)

        // 로그인 성공 시 세션에 로그인 정보 저장
        request.session.setAttribute(LOGIN_SESSION, true)
    }

    /**
     * 관리자 로그아웃
     */
    @DeleteMapping(value = ["/sign-out"])
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun signOut(
        request: HttpServletRequest
    ) {
        // session을 조회하여 로그인 되어있는지 확인
        val session = request.session.getAttribute(LOGIN_SESSION)
        if (session == null || !(session as Boolean)) {
            throw Exception("Not logged in")
        }

        // 로그아웃 성공 시 세션에 로그인 정보 삭제
        request.session.setAttribute(LOGIN_SESSION, false)
    }

    /**
     * 관리자 설정 조회
     * @return List<Response>: 관리자 설정 리스트
     */
    @GetMapping(value = ["/config"])
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    fun getAdminConfigs(): List<Response> {
        return adminConfigService.getAdminConfigs()
    }

    /**
     * 관리자 설정 수정 혹은 추가
     * @param datas: List<ConfigData>: 관리자 설정 수정 / 생성 리스트
     * @return List<ConfigData>: 관리자 설정 리스트
     */
    @PatchMapping(value = ["/config"])
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @IsAuthenticated
    fun upsertAdminConfigs(
        @RequestBody datas: List<Request>
    ): List<Response> {
        return adminConfigService.upsertAdminConfigs(datas)
    }
}