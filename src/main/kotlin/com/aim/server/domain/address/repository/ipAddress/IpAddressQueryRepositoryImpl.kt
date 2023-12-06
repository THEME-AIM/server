package com.aim.server.domain.address.repository.ipAddress

import com.aim.server.domain.address.dto.IpAddressData.IpAddressWithFloor
import com.aim.server.domain.address.entity.IpAddress
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import java.util.*
import com.aim.server.domain.address.entity.QIpAddress.ipAddress1 as ipAddress


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

    override fun findAllIpAddressWithFloor(): List<IpAddressWithFloor> {
        return queryFactory
            .select(
                Projections.constructor(
                    IpAddressWithFloor::class.java,
                    ipAddress.floor,
                    ipAddress.ipAddress
                )
            )
            .from(ipAddress)
            .fetch()
    }

    override fun updateIpAddressFloor(floor: Int, ipAddresses: List<String>) {
        if (ipAddresses.isEmpty()) return
        queryFactory
            .update(ipAddress)
            .set(ipAddress.floor, floor)
            .where(ipAddress.ipAddress.`in`(ipAddresses))
            .execute()
    }

    override fun deleteByIpAddresses(ipAddresses: List<String>) {
        if (ipAddresses.isEmpty()) return
        queryFactory
            .delete(ipAddress)
            .where(ipAddress.ipAddress.`in`(ipAddresses))
            .execute()
    }
}