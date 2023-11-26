package com.aim.server.domain.address.repository

import com.aim.server.domain.address.entity.AddressInfo
import com.aim.server.domain.address.entity.QAddressInfo.addressInfo
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.*

class AddressInfoQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : AddressInfoQueryRepository {
    override fun findByIpAddress(ipAddress: List<String>): List<AddressInfo> {
        return queryFactory
            .select(addressInfo)
            .from(addressInfo)
            .where(addressInfo.ipAddress.`in`(ipAddress))
            .fetch()

    }

    override fun deleteByIpAddress(ipAddress: String) {
        queryFactory
            .delete(addressInfo)
            .where(addressInfo.ipAddress.eq(ipAddress))
            .execute()
    }

    override fun getFloorList(): List<Int> {
        return queryFactory
            .select(addressInfo.floor)
            .from(addressInfo)
            .fetch()
    }

    override fun getDeptList(): List<String> {
        return queryFactory
            .select(addressInfo.department)
            .from(addressInfo)
            .fetch()
    }


}