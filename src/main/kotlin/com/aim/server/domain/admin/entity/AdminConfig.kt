package com.aim.server.domain.admin.entity

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "admin_config")
data class AdminConfig(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "key", unique = true)
    val key: String,

    @Column(name = "value")
    val value: String,
)

