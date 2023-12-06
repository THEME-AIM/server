package com.aim.server.domain.address.repository.ipAddress

import com.aim.server.domain.address.entity.IpAddress
import java.util.Optional

interface IpAddressQueryRepository {
    fun updateIpAddress(value: String, isUsed: Boolean)
    fun findByIpAddress(value: String): Optional<IpAddress>

    fun findByUnusedIp() : List<IpAddress>
}
