package com.aim.server.domain.admin.service

import com.aim.server.core.exception.BaseException
import com.aim.server.core.exception.ErrorCode
import com.aim.server.domain.address.dto.IpAddressData
import com.aim.server.domain.address.repository.addressInfo.AddressInfoRepository
import com.aim.server.domain.address.repository.ipAddress.IpAddressBatchRepository
import com.aim.server.domain.address.repository.ipAddress.IpAddressRepository
import com.aim.server.domain.address.service.OpenStackNetworkService
import com.aim.server.domain.admin.const.ConfigConsts.Companion.ADMIN_PASSWORD_KEY
import com.aim.server.domain.admin.const.ConfigConsts.Companion.ADMIN_USERNAME_KEY
import com.aim.server.domain.admin.const.ConfigConsts.Companion.DEFAULT_ADMIN_PASSWORD_VALUE
import com.aim.server.domain.admin.const.ConfigConsts.Companion.DEFAULT_ADMIN_USERNAME_VALUE
import com.aim.server.domain.admin.const.ConfigConsts.Companion.FLOOR_PREFIX
import com.aim.server.domain.admin.const.ConfigConsts.Companion.OPENSTACK_NETWORK_NAME_KEY
import com.aim.server.domain.admin.dto.AdminConfigData.*
import com.aim.server.domain.admin.entity.AdminConfig
import com.aim.server.domain.admin.repository.AdminConfigRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.validation.Errors


@Service
class AdminConfigServiceImpl(
    private val adminConfigRepository: AdminConfigRepository,
    private val ipAddressRepository: IpAddressRepository,
    private val ipAddressBatchRepository: IpAddressBatchRepository,
    private val passwordEncoder: PasswordEncoder,
    private val addressInfoRepository: AddressInfoRepository,
    private val openStackNetworkService: OpenStackNetworkService
) : AdminConfigService {
    private val log = KotlinLogging.logger { }

    /**
     * 서버 생성 시 자동으로 관리자 계정을 생성함. 계정 ID, Password는 admin으로 설정됨.
     */
    @PostConstruct
    private fun createDefaultAdminUser() {
        val username = adminConfigRepository.findValueByKey(ADMIN_USERNAME_KEY)
        val password = adminConfigRepository.findValueByKey(ADMIN_PASSWORD_KEY)

        if (username.isEmpty || password.isEmpty) {
            val adminUsername = AdminConfig(
                key = ADMIN_USERNAME_KEY,
                value = DEFAULT_ADMIN_USERNAME_VALUE
            )
            val adminPassword = AdminConfig(
                key = ADMIN_PASSWORD_KEY,
                value = passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD_VALUE)
            )
            adminConfigRepository.save(adminUsername)
            adminConfigRepository.save(adminPassword)
        }
    }

    /**
     * 관리자 로그인 정보를 바탕으로 로그인을 진행함. 예외 발생 시 로그인 실패
     * @param signIn: SignInRequest: 관리자 로그인 정보 (ID / Password)
     */
    override fun signIn(signIn: SignInRequest, errors: Errors) {
        val username = adminConfigRepository.findValueByKey(ADMIN_USERNAME_KEY).orElseThrow {
            BaseException(ErrorCode.INVALID_ID_OR_PASSWORD)
        }
        val password = adminConfigRepository.findValueByKey(ADMIN_PASSWORD_KEY).orElseThrow {
            BaseException(ErrorCode.INVALID_ID_OR_PASSWORD)
        }

        log.debug { "로그인 요청: username: ${signIn.username}, password: ${signIn.password} findUsername: $username, findPassword: $password" }

        if (username.isEmpty() || username != signIn.username) {
            errors.rejectValue("username", "invalid", "ID가 일치하지 않습니다.")
        }

        if (password.isEmpty() || !passwordEncoder.matches(signIn.password, password)) {
            errors.rejectValue("password", "invalid", "비밀번호가 일치하지 않습니다.")
        }
    }

    /**
     * 관리자 설정 전체 조회
     * @return List<ConfigData>: 관리자 설정 리스트
     */
    override fun getAdminConfigs(): List<APIResponse> = adminConfigRepository.findAllAdminKeys().map { it.toResponse() }

    /**
     * 관리자 설정 수정 혹은 추가
     * @param configs: List<ConfigData>: 수정 혹은 생성할 관리자 설정 리스트
     * @return List<ConfigData>: 수정 혹은 생성된 관리자 설정 리스트
     */
    @Transactional
    override fun upsertAdminConfigs(configs: List<AdminKeys>): List<APIResponse> = configs.run {
        val findConfigs = adminConfigRepository.findByKeyIn(this.map(AdminKeys::key))
        return this.map {
            findConfigs.find { findConfig -> findConfig.key == it.key }?.apply {
                this.value = convertValue(it.key, it.value)
            } ?: adminConfigRepository.save(AdminConfig(key = it.key, value = convertValue(it.key, it.value)))
        }.map { it.toResponse() }
    }

    /**
     * 층별 IP Address 설정 및 IpAddress Entity 데이터 생성, 수정 및 삭제
     * @param configs: List<FloorConfigData>: 층별 IP Address 설정 리스트
     * @return List<ConfigData>: 수정 혹은 생성된 관리자 설정 리스트
     */
    @Transactional
    override fun upsertFloorConfigs(configs: List<FloorKeys>): List<APIResponse> {
        // IP Address 전체 조회
        val findIpAddressWithFloor = ipAddressRepository.findAllIpAddressWithFloor()
        val findIpAddresses = findIpAddressWithFloor.map { it.ipAddress }.toSet()
        val usedIpAddresses = mutableSetOf<String>()
        configs.forEach {
            val ipAddresses = FloorKeys.betweenIpAddress(it.startIpAddress, it.endIpAddress)
            ipAddressRepository.updateIpAddressFloor(
                it.floor,
                findIpAddressWithFloor.filter { ipAddress ->
                    ipAddress.ipAddress in ipAddresses.intersect(
                        findIpAddresses
                    ) && ipAddress.floor != it.floor
                }.map { ipAddress ->
                    ipAddress.ipAddress
                }
            )
            ipAddressBatchRepository.batchInsert(ipAddresses.subtract(findIpAddresses).map { ipAddress ->
                IpAddressData.IpAddressWithFloor(
                    ipAddress = ipAddress,
                    floor = it.floor
                )
            })
            usedIpAddresses.addAll(ipAddresses)
        }
        val deleteIpAddresses = findIpAddresses.subtract(usedIpAddresses)
        if (deleteIpAddresses.isNotEmpty()) {
            deleteIpAddresses.forEach {
                addressInfoRepository.deleteByIpAddress(it)
            }
            ipAddressRepository.deleteByIpAddresses(deleteIpAddresses.toList())
        }
        return upsertAdminConfigs(configs.toAdminKeys())
    }

    override fun createIpAddressConfig(config: IpAddressRequest): List<APIResponse> {
        val createdNetwork = openStackNetworkService.createNetwork(
            "main_network_server",
            config.startIpAddress,
            config.endIpAddress,
            config.gateway,
            config.cidr
        )
        val keys = config.toKeys() + AdminKeys(
            key = OPENSTACK_NETWORK_NAME_KEY,
            value = createdNetwork.id
        )
        return upsertAdminConfigs(keys)
    }

    /**
     * 층별 IPAddress 설정 리스트를 관리자 설정 리스트로 변환
     */
    private fun List<FloorKeys>.toAdminKeys(): List<AdminKeys> {
        val adminKeys = mutableListOf<AdminKeys>()
        this.forEach {
            adminKeys.add(AdminKeys(key = "${FLOOR_PREFIX}start_ip_address_${it.floor}F", value = it.startIpAddress))
            adminKeys.add(AdminKeys(key = "${FLOOR_PREFIX}end_ip_address_${it.floor}F", value = it.endIpAddress))
        }
        return adminKeys
    }

    /**
     * 관리자 설정 값 로직에 알맞게 변환
     * @param key: String: 관리자 설정 키
     * @param value: String: 관리자 설정 값
     * @return String: 변환된 관리자 설정 값
     */
    private fun convertValue(key: String, value: String): String = when (key) {
        ADMIN_PASSWORD_KEY -> passwordEncoder.encode(value)
        else -> value
    }

    /**
     * 관리자 설정 특정 키 조회
     * @param key: String: 관리자 설정 키
     * @return String: 관리자 설정 값
     */
    override fun getAdminConfig(key: String): String = adminConfigRepository.findValueByKey(key).orElseThrow {
        BaseException(ErrorCode.CONFIG_NOT_FOUND)
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
        if (key == ADMIN_PASSWORD_KEY) {
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