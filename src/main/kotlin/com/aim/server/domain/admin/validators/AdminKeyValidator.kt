package com.aim.server.domain.admin.validators

import com.aim.server.domain.admin.annotation.AdminKey
import com.aim.server.domain.admin.const.ConfigConsts.Companion.ADMIN_CONFIG_PREFIX
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class AdminKeyValidator : ConstraintValidator<AdminKey, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value?.startsWith(ADMIN_CONFIG_PREFIX) ?: false
    }
}