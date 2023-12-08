package com.aim.server.domain.address.service

import com.aim.server.core.exception.BaseException
import com.aim.server.core.exception.ErrorCode
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

        val tmpList = matchedAddressInfo.map { it.ipAddress.ipAddress }.toList()

        addressInfoRepository.setAttributeEmpty(tmpList)

        //TODO: 왜 아래 코드는 update가 제대로 일어나지 않는가

//        addressInfo.map {
//            matchedAddressInfo.find { matchedAddressInfo ->
//                matchedAddressInfo.ipAddress.ipAddress == it.ipAddress
//            }?.apply {
//                this.macAddress = it.macAddress
//                this.department = it.department
//                this.ipAddress.floor = it.floor
//                this.name = it.name
//                this.isComputer = it.isComputer
//            } ?: run {
//                this.insertAddressInfo(it)
//            }
//        }

        addressInfo.forEach {
            if (tmpList.contains(it.ipAddress)) {
                if(!addressInfoRepository.checkDuplicateMacAddress(it.macAddress).isEmpty) {
                    throw BaseException(ErrorCode.MAC_ADDRESS_ALREADY_EXISTS)
                }
                addressInfoRepository.updateAddressInfo(it)
            } else {
                this.insertAddressInfo(it)
            }
        }
    }

    @Transactional
    override fun insertAddressInfo(addressInfo: AddressInfoData) {

        val ipAddress: Optional<IpAddress> = ipAddressRepository.findByIpAddress(addressInfo.ipAddress)

        if (ipAddress.isEmpty) {
            throw BaseException(ErrorCode.IP_ADDRESS_NOT_FOUND)
        } else if (ipAddress.get().isAssigned) {
            throw BaseException(ErrorCode.IP_ADDRESS_ALREADY_EXISTS)
        }
        ipAddressRepository.updateIpAddress(addressInfo.ipAddress, true)

        if (!addressInfoRepository.checkDuplicateMacAddress(addressInfo.macAddress).isEmpty) {
            throw BaseException(ErrorCode.MAC_ADDRESS_ALREADY_EXISTS)
        }

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
        ipAddressRepository.updateIpAddress(ipAddress, false)
    }
}