package com.aim.server.domain.address.repository

import com.aim.server.domain.address.entity.AddressInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
interface AddressInfoRepository: JpaRepository<AddressInfo, Long>, AddressInfoQueryRepository {
}
