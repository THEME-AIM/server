package com.aim.server.domain.address.repository.addressInfo

import com.aim.server.domain.address.entity.AddressInfo

interface AddressInfoQueryRepository {
    fun findByIpAddress(ipAddress: List<String>): List<AddressInfo>
    fun deleteByIpAddress(ipAddress: String)
    fun checkDuplicateMacAddress(macAddress: String): Boolean
}
