package com.aim.server.domain.address.service

import com.aim.server.domain.address.dto.AddressInfoData
import com.aim.server.domain.address.repository.addressInfo.AddressInfoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddressServiceImpl(
    private val addressInfoRepository: AddressInfoRepository
) : AddressService {
    override fun upsertAddressInfo(addressInfo: List<AddressInfoData>) {
        val matchedAddressInfo = addressInfoRepository.findByIpAddress(addressInfo.map { it.ipAddress })
        addressInfo.map {
            matchedAddressInfo.find { matchedAddressInfo ->
                matchedAddressInfo.ipAddress == it.ipAddress
            }?.let { matchedAddressInfo ->
                matchedAddressInfo.macAddress = it.macAddress
                matchedAddressInfo.department = it.department
                matchedAddressInfo.floor = it.floor
                matchedAddressInfo.name = it.name
                matchedAddressInfo.isComputer = it.isComputer
            } ?: addressInfoRepository.save(it.toEntity())
        }
    }

    override fun insertAddressInfo(addressInfo: AddressInfoData) {
        addressInfoRepository.save(addressInfo.toEntity())
    }

    override fun updateAddressInfo(addressInfo: AddressInfoData, ipAddress: String) {
        addressInfoRepository.save(addressInfoRepository.findByIpAddress(listOf(ipAddress))[0].apply {
            this.macAddress = addressInfo.macAddress
            this.department = addressInfo.department
            this.floor = addressInfo.floor
            this.name = addressInfo.name
            this.isComputer = addressInfo.isComputer
        })
    }

    @Transactional
    override fun deleteAddressInfo(ipAddress: String) {
        addressInfoRepository.deleteByIpAddress(ipAddress)
    }
}