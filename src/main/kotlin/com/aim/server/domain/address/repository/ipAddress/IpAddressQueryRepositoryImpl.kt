package com.aim.server.domain.address.repository.ipAddress

import com.aim.server.domain.address.entity.IpAddress
import com.aim.server.domain.address.entity.QIpAddress.ipAddress1 as ipAddress
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.Optional


class IpAddressQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : IpAddressQueryRepository {

    override fun updateIpAddress(value: String, isUsed: Boolean) {
        queryFactory
            .update(ipAddress)
            .set(ipAddress.isAssigned, isUsed)
            .where(ipAddress.ipAddress.eq(value))
            .execute()
    }

    override fun findByIpAddress(value: String): Optional<IpAddress> {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(ipAddress)
                .where(ipAddress.ipAddress.eq(value))
                .fetchOne()
        )
    }

    override fun findByUnusedIp(): List<IpAddress> {
        return queryFactory
            .select(ipAddress)
            .from(ipAddress)
            .where(ipAddress.isAssigned.eq(false))
            .fetch()
    }
}