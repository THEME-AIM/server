package com.aim.server.domain.openstack.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import java.time.Duration
import java.time.LocalDateTime

@Entity
@Table(name = "custom_servers")
data class CustomServer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "server_name", nullable = false)
    val serverName: String,

    @Column(name = "network_name", nullable = false)
    val networkName: String,

    @Column(name = "ipAddress", nullable = false)
    val ipAddress: String,

    @Column(name = "start_time", nullable = false)
    var startTime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "network_id", nullable = false)
    val networkId: String,

    @OneToOne
    @JoinColumn(name = "project_id", nullable = false, referencedColumnName = "project_id")
    val project: CustomProject,

    @Column(name = "upTime", nullable = false)
    var upTime: Duration = Duration.ZERO,

    @Column(name = "imageName", nullable = false)
    val imageName: String,

    @Column(name = "create_at", nullable = false)
    @CreatedBy
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "is_stopped", columnDefinition = "boolean")
    var isStopped: Boolean = false,

    @Column(name = "enabled", columnDefinition = "boolean")
    var enabled: Boolean = true,
)
