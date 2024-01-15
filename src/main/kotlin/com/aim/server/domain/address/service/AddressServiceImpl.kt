package com.aim.server.domain.address.service

import com.aim.server.core.exception.BaseException
import com.aim.server.core.exception.ErrorCode
import com.aim.server.domain.address.dto.AddressInfoData
import com.aim.server.domain.address.dto.AddressInfoResponse
import com.aim.server.domain.address.dto.IpAddressData
import com.aim.server.domain.address.entity.IpAddress
import com.aim.server.domain.address.repository.addressInfo.AddressInfoRepository
import com.aim.server.domain.address.repository.ipAddress.IpAddressRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AddressServiceImpl(
    private val addressInfoRepository: AddressInfoRepository,
    private val ipAddressRepository: IpAddressRepository,
    private val openStackNetworkService: OpenStackNetworkService
) : AddressService {

    @Transactional
    override fun upsertAddressInfo(addressInfo: List<AddressInfoData>) {
        val matchedAddressInfo = addressInfoRepository.findByIpAddress(addressInfo.map { it.ipAddress })
        val tmpList = matchedAddressInfo.map { it.ipAddress.ipAddress }.toList()
        addressInfoRepository.setAttributeEmpty(tmpList)
        addressInfo.forEach {
            if (tmpList.contains(it.ipAddress)) {
                if (!addressInfoRepository.checkDuplicateMacAddress(it.macAddress).isEmpty) {
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
        val instance =
            openStackNetworkService.createIpInstance(addressInfo.department, addressInfo.name, addressInfo.ipAddress)
        addressInfoRepository.save(addressInfo.toEntity(ipAddress = ipAddress.get()).apply { this.serverId = instance })
    }

    @Transactional
    override fun updateAddressInfo(addressInfo: AddressInfoData, ipAddress: String) {

        // macAddress 중복 체크
        if (!addressInfoRepository.checkDuplicateMacAddress(addressInfo.macAddress).isEmpty) {
            throw BaseException(ErrorCode.MAC_ADDRESS_ALREADY_EXISTS)
        }

        // ip_address 테이블에 변경하고자 하는 ip가 있는지 확인
        val status: IpAddress = ipAddressRepository.findByIpAddress(addressInfo.ipAddress).orElseThrow {
            throw BaseException(ErrorCode.IP_ADDRESS_NOT_FOUND)
        }


        // ip_address 테이블에 변경하고자 하는 ip가 사용가능한가 확인
        if (status.isAssigned) {
            throw BaseException(ErrorCode.IP_ADDRESS_ALREADY_EXISTS)
        }

        ipAddressRepository.updateIpAddress(ipAddress, false)
        ipAddressRepository.updateIpAddress(addressInfo.ipAddress, true)

        addressInfoRepository.updateAddressInfoV2(
            addressInfoRepository.findByIpAddress(listOf(ipAddress)).first().id,
            addressInfo
        )
        addressInfoRepository.findByIpAddress(listOf(ipAddress))
            .forEach {
                openStackNetworkService.updateIpInstance(it.serverId!!, addressInfo.ipAddress)
            }

    }

    @Transactional
    override fun deleteAddressInfo(ipAddress: String) {
        addressInfoRepository.findByIpAddress(listOf(ipAddress))
            .forEach {
                openStackNetworkService.deleteIpInstance(it.serverId!!)
                addressInfoRepository.delete(it)
            }
        ipAddressRepository.updateIpAddress(ipAddress, false)
    }


    override fun getAddressInfo(type: String): List<AddressInfoResponse> {
        val allAddressList: List<AddressInfoData> = addressInfoRepository.findAll().map {
            it.toDto()
        }

        return when (type) {
            "floor" -> {
                val groupedByFloor = allAddressList.groupBy { it.floor.toString() }
                groupedByFloor.map { (floor, addresses) ->
                    AddressInfoResponse(floor, addresses.toMutableList())
                }
            }

            "dept" -> {
                val groupedByDept = allAddressList.groupBy { it.department }
                groupedByDept.map { (dept, addresses) ->
                    AddressInfoResponse(dept, addresses.toMutableList())
                }
            }

            else -> throw BaseException(ErrorCode.INVALID_INPUT_VALUE)
        }
    }

    override fun searchAddressInfo(keyword: String, value: String): List<AddressInfoData> {
        return when (keyword) {
            "ip" -> addressInfoRepository.findAllByIpAddress(value).map { it.toDto() }
            "mac" -> addressInfoRepository.findAllByMac(value).map { it.toDto() }
            "name" -> addressInfoRepository.findAllByName(value).map { it.toDto() }
            else -> throw BaseException(ErrorCode.INVALID_INPUT_VALUE)
        }
    }


    override fun getRemainedAddress(): List<IpAddressData.IpAddressWithFloor> {
        val allUnusedAddressList: List<IpAddressData.IpAddressWithFloor> = ipAddressRepository.findByUnusedIp().map {
            it.toDto()
        }
        return allUnusedAddressList

    }

}