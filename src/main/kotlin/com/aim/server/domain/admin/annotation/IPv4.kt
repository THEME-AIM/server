package com.aim.server.domain.admin.annotation

import com.aim.server.domain.admin.validators.IPv4Validator
import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [IPv4Validator::class])
annotation class IPv4(
    val message: String = "정상적인 IP주소가 아닙니다. 다시 한번 확인해주세요.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
)