package com.aim.server.domain.address.repository.ipAddress

import com.aim.server.domain.address.dto.IpAddressData.IpAddressWithFloor
import com.aim.server.domain.address.entity.IpAddress
import java.util.*

interface IpAddressQueryRepository {
    fun updateIpAddress(value: String, isUsed: Boolean)
    fun findByIpAddress(value: String): Optional<IpAddress>
    fun findAllIpAddressWithFloor(): List<IpAddressWithFloor>
    fun updateIpAddressFloor(floor: Int, ipAddresses: List<String>)
    fun deleteByIpAddresses(ipAddress: List<String>)
}
