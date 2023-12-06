package com.aim.server.domain.admin.const

class ConfigConsts {
    companion object {
        const val LOGIN_SESSION = "login_session"
        private const val ADMIN_CONFIG_PREFIX = "admin."
        const val ADMIN_USERNAME_KEY = ADMIN_CONFIG_PREFIX + "username"
        const val ADMIN_PASSWORD_KEY = ADMIN_CONFIG_PREFIX + "password"
        const val START_IP_ADDRESS_KEY = ADMIN_CONFIG_PREFIX + "start_ip_address"
        const val END_IP_ADDRESS_KEY = ADMIN_CONFIG_PREFIX + "end_ip_address"
        const val GATEWAY_IP_ADDRESS_KEY = ADMIN_CONFIG_PREFIX + "gateway_ip_address"
        const val SUBNET_MASK_KEY = ADMIN_CONFIG_PREFIX + "subnet_mask_key"
        const val FLOOR_PREFIX = ADMIN_CONFIG_PREFIX + "floor_"
        const val DEFAULT_ADMIN_USERNAME_VALUE = "admin"
        const val DEFAULT_ADMIN_PASSWORD_VALUE = "password123!"
    }
}