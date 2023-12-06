package com.aim.server.domain.admin.validators

import com.aim.server.domain.admin.annotation.AdminKey
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class AdminKeyValidator : ConstraintValidator<AdminKey, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value?.startsWith("admin.") ?: false
    }
}