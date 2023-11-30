package com.aim.server.domain.address.repository.ipAddress

import com.aim.server.domain.address.entity.IpAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
interface IpAddressRepository: JpaRepository<IpAddress, Long>, IpAddressQueryRepository {
}
