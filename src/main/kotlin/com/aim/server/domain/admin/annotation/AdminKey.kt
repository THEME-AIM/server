package com.aim.server.domain.admin.annotation

import com.aim.server.domain.admin.validators.AdminKeyValidator
import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AdminKeyValidator::class])
annotation class AdminKey(
    val message: String = "정상적인 관리자 키가 아닙니다. 다시 한번 확인해주세요.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
)
