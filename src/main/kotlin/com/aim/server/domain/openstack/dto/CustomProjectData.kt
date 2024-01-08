package com.aim.server.domain.openstack.dto

import com.aim.server.domain.address.entity.IpAddress
import com.aim.server.domain.openstack.entity.CustomFlavor
import com.aim.server.domain.openstack.entity.CustomProject

class CustomProjectData {
    data class Create(
        val name: String,
        val numOfInstances: Int,
        val description: String,
        val imageName: String,
        val cost: Int,
        val flavor: CustomFlavor,
        val ipAddress: String,
    ) {
        fun toEntity(ipAddress: IpAddress) = CustomProject(
            name = name,
            numOfInstances = numOfInstances,
            description = description,
            imageName = imageName,
            flavor = flavor,
            cost = cost,
            ipAddress = ipAddress,
        )
    }

    data class Response(
        val id: Long,
        val name: String,
        val numOfInstances: Int,
        val description: String,
        val imageName: String,
        val flavor: CustomFlavor,
        val cost: Int,
        val enabled: Boolean,
    ) {
        companion object {
            fun fromEntity(entity: CustomProject) = Response(
                id = entity.id,
                name = entity.name,
                numOfInstances = entity.numOfInstances,
                description = entity.description,
                imageName = entity.imageName,
                flavor = entity.flavor,
                cost = entity.cost,
                enabled = entity.enabled,
            )
        }
    }
}