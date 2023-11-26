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

    @Column(name = "name", nullable = true)
    var name: String,

    @Column(name = "floor", nullable = true)
    var floor: Int,

    @Column(name = "department", nullable = true)
    var department: String,

    @Column(name = "is_computer", columnDefinition = "BOOLEAN")
    var isComputer: Boolean = true,

    @Column(name = "is_using", columnDefinition = "BOOLEAN")
    var isUsing: Boolean = false
) {
}
