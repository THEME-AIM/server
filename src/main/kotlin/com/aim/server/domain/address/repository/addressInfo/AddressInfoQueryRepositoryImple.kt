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
            .select(addressInfo.floor).distinct()
            .from(addressInfo)
            .fetch()
    }

    override fun getDeptList(): List<String> {
        return queryFactory
            .select(addressInfo.department).distinct()
            .from(addressInfo)
            .fetch()
    }

    override fun findAllByIpAddress(ipAddress: String): List<AddressInfo> {
        return queryFactory
            .select(addressInfo)
            .from(addressInfo)
            .where(addressInfo.ipAddress.eq(ipAddress))
            .fetch()
    }

    override fun findAllByName(name: String): List<AddressInfo> {
        return queryFactory
            .select(addressInfo)
            .from(addressInfo)
            .where(addressInfo.name.eq(name))
            .fetch()
    }

    override fun findAllByMac(mac: String): List<AddressInfo> {
        return queryFactory
            .select(addressInfo)
            .from(addressInfo)
            .where(addressInfo.macAddress.eq(mac))
            .fetch()
    }


}