package com.aim.server.domain.address.repository.addressInfo

import com.aim.server.domain.address.entity.AddressInfo
import com.aim.server.domain.address.entity.QAddressInfo.addressInfo
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.Optional

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

    override fun checkDuplicateMacAddress(macAddress: String): Optional<AddressInfo> {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(addressInfo)
                .where(addressInfo.macAddress.eq(macAddress))
                .fetchOne()
        )
    }

    override fun setAttributeEmpty(tmpList: List<String>) {
        queryFactory
            .update(addressInfo)
            .setNull(addressInfo.macAddress)
            .set(addressInfo.department, "")
            .set(addressInfo.name, "")
            .set(addressInfo.isComputer, true)
            .where(addressInfo.ipAddress.ipAddress.`in`(tmpList))
            .execute()
    }
}