package com.aim.server.domain.admin.validators

import com.aim.server.domain.admin.annotation.IPv4
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class IPv4Validator : ConstraintValidator<IPv4, String> {
    private val ipv4Pattern: Pattern = Pattern.compile(
        ("^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$")
    )

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return value?.let { ipv4Pattern.matcher(it).matches() } ?: false
    }
}