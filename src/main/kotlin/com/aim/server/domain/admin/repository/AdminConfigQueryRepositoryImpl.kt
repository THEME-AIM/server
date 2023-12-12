package com.aim.server.domain.admin.repository

import com.aim.server.domain.admin.entity.AdminConfig
import com.aim.server.domain.admin.entity.QAdminConfig.adminConfig
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AdminConfigQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : AdminConfigQueryRepository {
    override fun findValueByKey(key: String): Optional<String> {
        return Optional.ofNullable(
            queryFactory
                .select(adminConfig.value)
                .from(adminConfig)
                .where(adminConfig.key.eq(key))
                .fetchOne()
        )
    }

    override fun findAllAdminKeys(): List<AdminConfig> {
        return queryFactory
            .selectFrom(adminConfig)
            .where(adminConfig.key.notIn("username", "password"))
            .fetch()
    }
}