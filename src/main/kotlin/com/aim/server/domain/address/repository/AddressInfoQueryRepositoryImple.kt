package com.aim.server.domain.address.repository

import com.aim.server.domain.address.entity.AddressInfo
import com.aim.server.domain.address.entity.QAddressInfo.addressInfo
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.*

class AddressInfoQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : AddressInfoQueryRepository {
    override fun findByIpAddress(ipAddress: String): Optional<AddressInfo> {
        return Optional.ofNullable(
            queryFactory
                .select(addressInfo)
                .from(addressInfo)
                .where(addressInfo.ipAddress.eq(ipAddress))
                .fetchOne()
        )
    }

    override fun deleteByIpAddress(ipAddress: String) {
        queryFactory
            .delete(addressInfo)
            .where(addressInfo.ipAddress.eq(ipAddress))
            .execute()
    }
}