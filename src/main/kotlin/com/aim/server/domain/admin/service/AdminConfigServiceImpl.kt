package com.aim.server.domain.admin.service

import com.aim.server.domain.admin.const.ConfigConsts.Companion.ADMIN_PASSWORD
import com.aim.server.domain.admin.const.ConfigConsts.Companion.ADMIN_USERNAME
import com.aim.server.domain.admin.dto.AdminConfigData
import com.aim.server.domain.admin.dto.AdminConfigData.Request
import com.aim.server.domain.admin.dto.AdminConfigData.Response
import com.aim.server.domain.admin.entity.AdminConfig
import com.aim.server.domain.admin.repository.AdminConfigRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class AdminConfigServiceImpl(
    private val adminConfigRepository: AdminConfigRepository,
    private val passwordEncoder: PasswordEncoder,
) : AdminConfigService {
    private val log = KotlinLogging.logger { }

    /**
     * 관리자 로그인 정보를 바탕으로 로그인을 진행함. 예외 발생 시 로그인 실패
     * @param signIn: SignInRequest: 관리자 로그인 정보 (ID / Password)
     */
    override fun signIn(signIn: AdminConfigData.SignInRequest) {
        val username = adminConfigRepository.findValueByKey(ADMIN_USERNAME).orElseThrow {
            log.error { "admin_username is not found in admin_config table" }
            Exception("admin_username is not found in admin_config table")
        }
        val password = adminConfigRepository.findValueByKey(ADMIN_PASSWORD).orElseThrow {
            log.error { "admin_password is not found in admin_config table" }
            Exception("admin_password is not found in admin_config table")
        }

        log.debug { "username: ${signIn.username}, password: ${signIn.password} findUsername: $username, findPassword: $password" }

        if (username != signIn.username || !passwordEncoder.matches(signIn.password, password)) {
            log.error { "Invalid username or password" }
            throw Exception("Invalid username or password")
        }
    }

    /**
     * 관리자 설정 전체 조회
     * @return List<ConfigData>: 관리자 설정 리스트
     */
    override fun getAdminConfigs(): List<Response> = adminConfigRepository.findAll().map { it.toResponse() }

    /**
     * 관리자 설정 수정 혹은 추가
     * @param configs: List<ConfigData>: 수정 혹은 생성할 관리자 설정 리스트
     * @return List<ConfigData>: 수정 혹은 생성된 관리자 설정 리스트
     */
    override fun upsertAdminConfigs(configs: List<Request>): List<Response> =
        adminConfigRepository.saveAll(configs.map {
            getOrCreateAdminConfig(it.key, it.value)
        }).map { it.toResponse() }

    /**
     * 관리자 설정 특정 키 조회
     * @param key: String: 관리자 설정 키
     * @return String: 관리자 설정 값
     */
    override fun getAdminConfig(key: String): String = adminConfigRepository.findValueByKey(key).orElseThrow {
        log.error { "$key is not found in admin_config table" }
        Exception("$key is not found in admin_config table")
    }

    /**
     * 관리자 설정 조회 혹은 생성 (키 값이 존재한다면 조회, 없다면 생성)
     * @param key: String: 관리자 설정 키
     * @param value: String: 관리자 설정 값
     * @return AdminConfig: 조회 혹은 생성된 관리자 설정
     */
    private fun getOrCreateAdminConfig(key: String, value: String): AdminConfig {
        // 특정한 키 값인 경우 값 변환 하여 저장 (비밀번호)
        var newValue = value
        if (key == ADMIN_PASSWORD) {
            newValue = passwordEncoder.encode(value)
        }
        log.debug { "키 값 변경 혹은 생성: $key = $newValue" }
        return adminConfigRepository.findByKey(key).apply {
            this.ifPresent {
                it.value = newValue
            }
        }.orElse(AdminConfig(key = key, value = newValue))
    }
}