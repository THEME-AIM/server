package com.aim.server.domain.address.service

import com.aim.server.core.exception.BaseException
import com.aim.server.core.exception.ErrorCode
import com.aim.server.domain.address.dto.AddressInfoData
import com.aim.server.domain.address.dto.AddressInfoResponse
import com.aim.server.domain.address.dto.IpAddressData
import com.aim.server.domain.address.entity.IpAddress
import com.aim.server.domain.address.repository.addressInfo.AddressInfoRepository
import com.aim.server.domain.address.repository.ipAddress.IpAddressRepository
import com.aim.server.domain.admin.repository.AdminConfigRepository
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AddressServiceImpl(
    private val addressInfoRepository: AddressInfoRepository,
    private val ipAddressRepository: IpAddressRepository,
    private val adminConfigRepository: AdminConfigRepository
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


    override fun getAddressInfo(type: String): List<AddressInfoResponse> {
        val allAddressList: List<AddressInfoData> = addressInfoRepository.findAll().map {
            it.toDto()
        }
        val returnAddressList: MutableList<AddressInfoResponse> = mutableListOf()
        if (type == "floor") {
            val floorList: List<Int> = addressInfoRepository.getFloorList()
            for (i in floorList) {
                var thisTurn = AddressInfoResponse(i.toString(), mutableListOf())
                for (tmp in allAddressList) {
                    if (i == tmp.floor) {
                        thisTurn.addressList.add(tmp)
                    }
                }
                returnAddressList.add(thisTurn)

            }

        } else if (type == "dept") {
            val deptList: List<String> = addressInfoRepository.getDeptList()
            for (i in deptList) {
                var thisTurn = AddressInfoResponse(i.toString(), mutableListOf())
                for (tmp in allAddressList) {
                    if (i == tmp.department) {
                        thisTurn.addressList.add(tmp)
                    }
                }
                returnAddressList.add(thisTurn)
            }

        }

        return returnAddressList

    }

    override fun searchAddressInfo(keyword: String, value: String): List<AddressInfoData> {
        var returnAddressList : List<AddressInfoData> = mutableListOf()
        if(keyword == "ip"){
            returnAddressList = addressInfoRepository.findAllByIpAddress(value).map { it.toDto() }
        }
        else if(keyword == "mac"){
            returnAddressList = addressInfoRepository.findAllByMac(value).map { it.toDto() }
        }
        else if(keyword == "name"){
            returnAddressList = addressInfoRepository.findAllByName(value).map { it.toDto() }
        }
        return returnAddressList
    }

    override fun getRemainedAddress(): List<IpAddressData.IpAddressWithFloor> {
        val allUnusedAddressList : List<IpAddressData.IpAddressWithFloor> = ipAddressRepository.findByUnusedIp().map{
            it.toDto()
        }

        return allUnusedAddressList

    }


}