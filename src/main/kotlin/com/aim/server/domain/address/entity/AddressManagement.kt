package com.aim.server.domain.address.entity

import jakarta.persistence.*

@Entity
@Table(
    name = "address_management",
    uniqueConstraints = [
        UniqueConstraint(
            name = "ip_address_management_unique_constraint",
            columnNames = ["ip_address"]
        )
    ]
)
data class AddressManagement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "ip_address")
    var ipAddress: String,

    @Column(name = "floor", nullable = true)
    var floor: Int,

    @Column(name = "is_assigned", columnDefinition = "BOOLEAN")
    var isAssigned: Boolean = false,

    @Column(name = "subnet_mask")
    var subnetMask: Int,
) {
}
