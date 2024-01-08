package com.aim.server.domain.openstack.service

import com.aim.server.domain.openstack.dto.CustomProjectData.Create
import com.aim.server.domain.openstack.dto.CustomProjectData.Response
import org.springframework.stereotype.Service

@Service
interface CustomProjectService {
    fun createProject(createData: Create)
    fun getProjectList(): List<Response>
}