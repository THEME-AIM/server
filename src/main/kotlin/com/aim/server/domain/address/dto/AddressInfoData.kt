package com.aim.server.domain.address.dto

import com.aim.server.domain.address.entity.AddressInfo
import com.aim.server.domain.address.entity.IpAddress

data class AddressInfoData(
    val ipAddress: String,
    val macAddress: String,
    val name: String,
    val floor: Int,
    val department: String,
    val isComputer: Boolean = true
) {
    fun toEntity(ipAddress: IpAddress): AddressInfo {
        return AddressInfo(
            ipAddress = ipAddress,
            macAddress = macAddress,
            name = name,
            department = department,
            isComputer = isComputer
        )
    }





}
