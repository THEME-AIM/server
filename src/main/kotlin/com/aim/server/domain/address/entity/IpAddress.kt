package com.aim.server.domain.address.entity

import com.aim.server.domain.address.dto.IpAddressData
import jakarta.persistence.*

@Entity
@Table(
    name = "ip_address",
    uniqueConstraints = [
        UniqueConstraint(
            name = "ip_address_unique_constraint",
            columnNames = ["ip_address"]
        )
    ]
)
@SequenceGenerator(
    name = "ip_address_sequence",
    sequenceName = "ip_address_sequence",
    initialValue = 1,
    allocationSize = 100
)
data class IpAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "ip_address")
    var ipAddress: String,

    @Column(name = "floor", nullable = true)
    var floor: Int,

    @Column(name = "is_assigned", columnDefinition = "BOOLEAN")
    var isAssigned: Boolean = false,

    ) {
    fun toDto() : IpAddressData.IpAddressWithFloor {
        return IpAddressData.IpAddressWithFloor(
            floor = floor,
            ipAddress = ipAddress
        )
    }
}
