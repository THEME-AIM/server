package com.aim.server.domain.openstack.entity

import com.aim.server.domain.address.entity.IpAddress
import jakarta.persistence.*
import org.springframework.boot.context.properties.bind.DefaultValue

@Entity
@Table(name = "projects")
data class CustomProject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    val id: Long = 0L,

    @Column(name = "name")
    val name: String,

    @Column(name = "num_of_instances")
    val numOfInstances: Int,

    @OneToOne
    @JoinColumn(name = "ip_address", referencedColumnName = "ip_address")
    val ipAddress: IpAddress,

    @Column(name = "description")
    val description: String,

    @Column(name = "image_name")
    val imageName: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "flavor")
    val flavor: CustomFlavor,

    @Column(name = "cost")
    val cost: Int,

    @Column(name = "enabled", columnDefinition = "boolean")
    @DefaultValue("true")
    val enabled: Boolean = true,
)
