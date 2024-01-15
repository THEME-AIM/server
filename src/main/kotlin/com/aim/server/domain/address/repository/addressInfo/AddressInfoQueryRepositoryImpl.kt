package com.aim.server.domain.address.repository.addressInfo

import com.aim.server.domain.address.dto.AddressInfoData
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
//            .set(addressInfo.department, "")
//            .set(addressInfo.name, "")
//            .set(addressInfo.isComputer, true)
            .where(addressInfo.ipAddress.ipAddress.`in`(tmpList))
            .execute()
    }

    override fun updateAddressInfo(addressInfoData: AddressInfoData) {
        queryFactory
            .update(addressInfo)
            .set(addressInfo.macAddress, addressInfoData.macAddress)
            .set(addressInfo.department, addressInfoData.department)
            .set(addressInfo.name, addressInfoData.name)
            .set(addressInfo.isComputer, addressInfoData.isComputer)
            .where(addressInfo.ipAddress.ipAddress.eq(addressInfoData.ipAddress))
            .execute()
    }

    override fun updateAddressInfoV2(id: Long, addressInfoData: AddressInfoData) {
        queryFactory
            .update(addressInfo)
            .set(addressInfo.macAddress, addressInfoData.macAddress)
            .set(addressInfo.department, addressInfoData.department)
            .set(addressInfo.name, addressInfoData.name)
            .set(addressInfo.isComputer, addressInfoData.isComputer)
            .set(addressInfo.ipAddress.ipAddress, addressInfoData.ipAddress)
            .where(addressInfo.id.eq(id))
            .execute()
    }

    override fun getFloorList(): List<Int> {
        return queryFactory
            .select(addressInfo.ipAddress.floor).distinct()
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
            .where(addressInfo.ipAddress.ipAddress.eq(ipAddress))
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