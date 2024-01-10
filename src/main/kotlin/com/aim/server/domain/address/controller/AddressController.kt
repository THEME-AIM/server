package com.aim.server.domain.address.controller

import com.aim.server.core.response.BaseResponse
import com.aim.server.core.response.SuccessResponse
import com.aim.server.domain.address.dto.AddressInfoData
import com.aim.server.domain.address.dto.AddressInfoResponse
import com.aim.server.domain.address.dto.IpAddressData
import com.aim.server.domain.address.service.AddressService
import com.aim.server.domain.address.service.OpenStackNetworkService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/address"])
class AddressController(
    private val addressService: AddressService,
    private val openStackNetworkService: OpenStackNetworkService
) {
    /**
     * IP 정보 업로드
     * @param AddressInfoData: IP 정보
     * @return Unit
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
//    @IsAuthenticated
    fun uploadIp(
        @RequestBody addressInfo: AddressInfoData
    ) {
        return addressService.insertAddressInfo(addressInfo)
    }

    @GetMapping(value = ["/test"])
    @ResponseBody
    fun test(): Any {
        println("run test1")
        val test = openStackNetworkService.createNetwork("test", "192.168.0")
        println("run test2")
        return test
    }

    /**
     * IP 정보 bulk 업로드
     * @param List<AddressInfoData>: IP 정보 리스트
     * @return Unit
     */
    @PostMapping(value = ["/bulk"])
    @ResponseStatus(value = HttpStatus.CREATED)
//    @IsAuthenticated
    fun uploadIpBulk(
        @RequestBody addressInfo: List<AddressInfoData>
    ) {
        return addressService.upsertAddressInfo(addressInfo)
    }

    /**
     * IP 정보 조회
     * @param String: IP 주소
     * @return Unit
     */
    @PatchMapping(value = ["/{ipAddress}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun updateIp(
        @PathVariable(value = "ipAddress") ipAddress: String,
        @RequestBody addressInfo: AddressInfoData
    ) {
        return addressService.updateAddressInfo(addressInfo, ipAddress)
    }

    /**
     * IP 정보 삭제
     * @param String: IP 주소
     * @return Unit
     */
    @DeleteMapping(value = ["/{ipAddress}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun deleteIp(
        @PathVariable ipAddress: String,
    ) {
        return addressService.deleteAddressInfo(ipAddress)
    }

    /**
     * IP 리스트 확인
     * @param String: IP 주소
     * @return Unit
     */
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    fun getIpList(
        @RequestParam type: String,
    ): BaseResponse<List<AddressInfoResponse>> {
        return SuccessResponse.of(addressService.getAddressInfo(type))
    }

    /**
     * 잔여 IP 확인
     * @param String: IP 주소
     * @return Unit
     */
    @GetMapping(value = ["/remained"])
    @ResponseStatus(value = HttpStatus.OK)
    fun getRemainedIp(): BaseResponse<List<IpAddressData.IpAddressWithFloor>> {
        return SuccessResponse.of(addressService.getRemainedAddress())
    }

    /**
     * 개인 IP 검색
     * @param String: IP 주소
     * @return Unit
     */
    // /api/addrress/search?keyword={키워드}&value={값}
    @GetMapping(value = ["/search"])
    @ResponseStatus(value = HttpStatus.OK)
    fun searchIp(
        @RequestParam keyword: String,
        @RequestParam value: String
    ): BaseResponse<List<AddressInfoData>> {
        return SuccessResponse.of(addressService.searchAddressInfo(keyword, value))

    }
}