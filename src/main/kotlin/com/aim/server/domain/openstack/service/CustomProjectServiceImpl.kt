package com.aim.server.domain.openstack.service

import com.aim.server.domain.address.repository.ipAddress.IpAddressRepository
import com.aim.server.domain.openstack.dto.CustomProjectData.Create
import com.aim.server.domain.openstack.dto.CustomProjectData.Response
import com.aim.server.domain.openstack.repository.CustomProjectRepository
import org.springframework.stereotype.Service

@Service
class CustomProjectServiceImpl(
    private val customProjectRepository: CustomProjectRepository,
    private val ipAddressRepository: IpAddressRepository
) : CustomProjectService {
    override fun createProject(createData: Create) {
        customProjectRepository.findCustomProjectByName(createData.name).ifPresent {
            throw Exception("이미 존재하는 프로젝트입니다.")
        }
        val ipAddress = ipAddressRepository.findByIpAddress(createData.ipAddress).orElseThrow {
            throw Exception("존재하지 않는 IP 주소입니다.")
        }
        customProjectRepository.save(createData.toEntity(ipAddress = ipAddress))
    }

    override fun getProjectList(): List<Response> {
        return customProjectRepository.findAll().map { Response.fromEntity(it) }
    }
}