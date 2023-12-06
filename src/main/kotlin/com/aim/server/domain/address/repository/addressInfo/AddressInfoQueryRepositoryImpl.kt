package com.aim.server.domain.address.repository.addressInfo

import com.aim.server.domain.address.entity.AddressInfo
import com.aim.server.domain.address.entity.QAddressInfo.addressInfo
import com.querydsl.jpa.impl.JPAQueryFactory

class AddressInfoQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : AddressInfoQueryRepository {
    override fun findByIpAddress(ipAddress: List<String>): List<AddressInfo> {
        return queryFactory
            .select(addressInfo)
            .from(addressInfo)
            .where(addressInfo.ipAddress.ipAddress.`in`(ipAddress))
            .fetch()

    }

    override fun deleteByIpAddress(ipAddress: String) {
        queryFactory
            .delete(addressInfo)
            .where(addressInfo.ipAddress.ipAddress.eq(ipAddress))
            .execute()
    }

    override fun checkDuplicateMacAddress(macAddress: String): Boolean {
        return queryFactory
            .select(addressInfo)
            .from(addressInfo)
            .where(addressInfo.macAddress.eq(macAddress))
            .fetchCount() > 0
    }
}