package com.aim.server.domain.address.entity

import jakarta.persistence.*

@Entity
@Table(
    name = "address_info",
    uniqueConstraints = [
        UniqueConstraint(
            name = "ip_address_unique_constraint",
            columnNames = ["ip_address"]
        ),
        UniqueConstraint(
            name = "mac_address_unique_constraint",
            columnNames = ["mac_address"]
        ),
    ]
)
data class AddressInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "ip_address")
    var ipAddress: String,

    @Column(name = "mac_address", nullable = true)
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
