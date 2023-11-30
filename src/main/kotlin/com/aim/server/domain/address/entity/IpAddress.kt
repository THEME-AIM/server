package com.aim.server.domain.address.entity

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
}
