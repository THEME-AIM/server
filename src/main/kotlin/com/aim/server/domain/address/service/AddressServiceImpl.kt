package com.aim.server.domain.address.service

import com.aim.server.domain.address.dto.AddressInfoData
import com.aim.server.domain.address.repository.addressInfo.AddressInfoRepository
import com.aim.server.domain.address.dto.AddressInfoResponse
import com.aim.server.domain.address.entity.AddressInfo
import com.aim.server.domain.admin.repository.AdminConfigRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddressServiceImpl(
    private val addressInfoRepository: AddressInfoRepository,
    private val adminConfigRepository: AdminConfigRepository
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

    override fun getRemainedAddress(): List<AddressInfoData> {
        val firstIp = adminConfigRepository.findValueByKey("start_ip_address").get().split(".")[3].toInt()
        val endIp = adminConfigRepository.findValueByKey("end_ip_address").get().split(".")[3].toInt()
        val frontIp = adminConfigRepository.findValueByKey("start_ip_address").get().substringBeforeLast('.')
        val allAddressList: List<AddressInfoData> = addressInfoRepository.findAll().map {
            it.toDto()
        }
        val firstToEndList = (firstIp..endIp).toMutableList()
        for(i in firstToEndList){
            var thisTurn = "$frontIp.$i"
            if(addressInfoRepository.findAllByIpAddress(thisTurn).isEmpty()) continue
            else firstToEndList.remove(i)
        }

        return allAddressList

    }


}