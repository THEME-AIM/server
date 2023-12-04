package com.aim.server.domain.address.entity

import jakarta.persistence.*

@Entity
@Table(
    name = "address_info",
    uniqueConstraints = [
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

    @OneToOne
    @JoinColumn(name = "ip_address", referencedColumnName = "ip_address")
    var ipAddress: IpAddress,

    @Column(name = "mac_address", nullable = true)
    var macAddress: String,

    @Column(name = "name", nullable = true)
    var name: String,

    @Column(name = "department", nullable = true)
    var department: String,

    @Column(name = "is_computer", columnDefinition = "BOOLEAN")
    var isComputer: Boolean = true,
) {
}
