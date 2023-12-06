package com.aim.server.domain.address.repository.addressInfo

import com.aim.server.domain.address.entity.AddressInfo

interface AddressInfoQueryRepository {
    fun findByIpAddress(ipAddress: List<String>): List<AddressInfo>
    fun deleteByIpAddress(ipAddress: String)
    fun getFloorList(): List<Int>
    fun getDeptList() : List<String>
    fun findAllByIpAddress(ipAddress: String) : List<AddressInfo>
    fun findAllByName(name : String) : List<AddressInfo>
    fun findAllByMac(mac : String) : List<AddressInfo>
}
