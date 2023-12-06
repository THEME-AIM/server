package com.aim.server.domain.admin.entity

import com.aim.server.domain.admin.dto.AdminConfigData.APIResponse
import jakarta.persistence.*

@Entity
@Table(
    name = "admin_config",
    uniqueConstraints = [
        UniqueConstraint(
            name = "key_unique_constraint",
            columnNames = ["key"]
        )
    ]
)
data class AdminConfig(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "key")
    val key: String,

    @Column(name = "value")
    var value: String,
) {
    fun toResponse() = APIResponse(
        id = id,
        key = key,
        value = value
    )
}

