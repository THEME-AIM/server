package com.aim.server.domain.address.entity

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
    val macAddress: String,
    val name: String,
    val floor: Int,
    val department: String,
    @Column(name = "is_computer", columnDefinition = "BOOLEAN")
    val isComputer: Boolean = true
)
