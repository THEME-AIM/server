package com.aim.server.domain.address.service

import com.aim.server.domain.address.dto.AddressInfoData
import com.aim.server.domain.address.entity.IpAddress
import com.aim.server.domain.address.repository.addressInfo.AddressInfoRepository
import com.aim.server.domain.address.repository.ipAddress.IpAddressRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AddressServiceImpl(
    private val addressInfoRepository: AddressInfoRepository,
    private val ipAddressRepository: IpAddressRepository
) : AddressService {
    @Transactional
    override fun upsertAddressInfo(addressInfo: List<AddressInfoData>) {
        val matchedAddressInfo = addressInfoRepository.findByIpAddress(addressInfo.map { it.ipAddress })
        addressInfo.map {
            matchedAddressInfo.find { matchedAddressInfo ->
                matchedAddressInfo.ipAddress.ipAddress == it.ipAddress
            }?.let { matchedAddressInfo ->
                addressInfoRepository.save(matchedAddressInfo.apply {
                    this.macAddress = it.macAddress
                    this.department = it.department
                    this.ipAddress.floor = it.floor
                    this.name = it.name
                    this.isComputer = it.isComputer
                })
            } ?: run {
                ipAddressRepository.updateIpAddress(it.ipAddress)
                val ipAddress: Optional<IpAddress> = ipAddressRepository.findByIpAddress(it.ipAddress)
                addressInfoRepository.save(it.toEntity(ipAddress = ipAddress.get()))
            }
        }
    }

    @Transactional
    override fun insertAddressInfo(addressInfo: AddressInfoData) {
        ipAddressRepository.updateIpAddress(addressInfo.ipAddress)
        val ipAddress: Optional<IpAddress> = ipAddressRepository.findByIpAddress(addressInfo.ipAddress)
        addressInfoRepository.save(addressInfo.toEntity(ipAddress = ipAddress.get()))
    }

    override fun updateAddressInfo(addressInfo: AddressInfoData, ipAddress: String) {
        addressInfoRepository.save(addressInfoRepository.findByIpAddress(listOf(ipAddress))[0].apply {
            this.macAddress = addressInfo.macAddress
            this.department = addressInfo.department
            this.ipAddress.floor = addressInfo.floor
            this.name = addressInfo.name
            this.isComputer = addressInfo.isComputer
        })
    }

    @Transactional
    override fun deleteAddressInfo(ipAddress: String) {
        addressInfoRepository.deleteByIpAddress(ipAddress)
    }
}