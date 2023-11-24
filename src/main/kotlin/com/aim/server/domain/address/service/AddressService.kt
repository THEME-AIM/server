package com.aim.server.domain.address.service

import com.aim.server.domain.address.dto.AddressInfoData


interface AddressService {
    fun upsertAddressInfo(addressInfo: List<AddressInfoData>)
    fun insertAddressInfo(addressInfo: AddressInfoData)
    fun updateAddressInfo(addressInfo: AddressInfoData, ipAddress: String)
    fun deleteAddressInfo(ipAddress: String)
}
