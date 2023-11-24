package com.aim.server.domain.address.repository

import com.aim.server.domain.address.entity.AddressInfo
import java.util.*

interface AddressInfoQueryRepository {
    fun findByIpAddress(ipAddress: List<String>): List<AddressInfo>
    fun deleteByIpAddress(ipAddress: String)
}
