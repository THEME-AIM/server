package com.aim.server.domain.address.service

import com.aim.server.domain.address.dto.AddressInfoData
import com.aim.server.domain.address.entity.AddressInfo
import com.aim.server.domain.address.repository.AddressInfoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddressServiceImpl(
    private val addressInfoRepository: AddressInfoRepository
): AddressService {
    override fun upsertAddressInfo(addressInfo: List<AddressInfoData>) {
        addressInfoRepository.saveAll(addressInfo.map {
            checkNeedToUpdate(it)
        })
    }

    override fun insertAddressInfo(addressInfo: AddressInfoData) {
        addressInfoRepository.save(addressInfo.toEntity())
    }

    override fun updateAddressInfo(addressInfo: AddressInfoData, ipAddress: String) {
        addressInfoRepository.save(addressInfoRepository.findByIpAddress(ipAddress).get().apply {
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

    private fun checkNeedToUpdate(addressInfoData: AddressInfoData): AddressInfo{
        return addressInfoRepository.findByIpAddress(addressInfoData.ipAddress).apply {
            this.ifPresent{
                it.ipAddress = addressInfoData.ipAddress
                it.macAddress = addressInfoData.macAddress
                it.department = addressInfoData.department
                it.floor = addressInfoData.floor
                it.name = addressInfoData.name
                it.isComputer = addressInfoData.isComputer
            }
        }.orElse(addressInfoData.toEntity())
    }
}