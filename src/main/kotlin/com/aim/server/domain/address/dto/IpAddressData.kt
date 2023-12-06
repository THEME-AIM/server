package com.aim.server.domain.address.dto

class IpAddressData {

    data class IpAddressWithFloor(
        val floor: Int,
        val ipAddress: String,
    ) {
        override fun equals(other: Any?): Boolean {
            return if (other is IpAddressWithFloor) {
                other.ipAddress == ipAddress && other.floor == floor
            } else {
                false
            }
        }


    }
}