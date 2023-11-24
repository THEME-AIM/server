package com.aim.server.domain.address.entity

import com.aim.server.domain.address.dto.AddressInfoData
import jakarta.persistence.*

@Entity
@Table(name = "address_info")
data class AddressInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "ip_address", unique = true)
    var ipAddress: String,

    @Column(name = "mac_address", unique = true, nullable = true)
    var macAddress: String,

    @Column(name = "name")
    var name: String,

    @Column(name = "floor")
    var floor: Int,

    @Column(name = "department")
    var department: String,

    @Column(name = "is_computer", columnDefinition = "BOOLEAN")
    var isComputer: Boolean = true
) {
}
