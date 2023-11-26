package com.aim.server.domain.address.dto

data class AddressInfoResponse(
    var key: String,
    var addressList: MutableList<AddressInfoData>
) {
    constructor() : this("", empty )
}
