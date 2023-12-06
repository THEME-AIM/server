package com.aim.server.domain.address.controller

import com.aim.server.core.annotation.IsAuthenticated
import com.aim.server.core.response.BaseResponse
import com.aim.server.core.response.SuccessResponse
import com.aim.server.domain.address.dto.AddressInfoData
import com.aim.server.domain.address.service.AddressService
import jakarta.websocket.server.PathParam
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/address"])
class AddressController(
    private val addressService: AddressService
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
    ): BaseResponse<Unit> {
        addressService.insertAddressInfo(addressInfo)
        return SuccessResponse(201, "IP 정보 업로드 성공", Unit)
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
    ): BaseResponse<Unit> {
        addressService.upsertAddressInfo(addressInfo)
        return SuccessResponse(201, "IP 정보 업로드 성공", Unit)
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
    ): BaseResponse<Unit> {
        addressService.updateAddressInfo(addressInfo, ipAddress)
        return SuccessResponse(200, "IP 정보 업데이트 성공", Unit)
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
    ): BaseResponse<Unit> {
        addressService.deleteAddressInfo(ipAddress)
        return SuccessResponse(200, "IP 정보 삭제 성공", Unit)
    }
}