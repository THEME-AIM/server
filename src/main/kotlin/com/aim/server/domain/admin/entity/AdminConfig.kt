package com.aim.server.domain.admin.entity

import com.aim.server.domain.admin.dto.AdminConfigData.Response
import jakarta.persistence.*

@Entity
@Table(name = "admin_config")
data class AdminConfig(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "key", unique = true)
    val key: String,

    @Column(name = "value")
    var value: String,
) {
    fun toResponse() = Response(
        id = id,
        key = key,
        value = value
    )
}

