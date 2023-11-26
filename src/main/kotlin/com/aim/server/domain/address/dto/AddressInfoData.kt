package com.aim.server.domain.address.dto

import com.aim.server.domain.address.entity.AddressInfo

data class AddressInfoData(
    val ipAddress: String,
    val macAddress: String,
    val name: String,
    val floor: Int,
    val department: String,
    val isComputer: Boolean = true
) {
    fun toEntity(): AddressInfo {
        return AddressInfo(
            ipAddress = ipAddress,
            macAddress = macAddress,
            name = name,
            floor = floor,
            department = department,
            isComputer = isComputer
        )
    }




}
