package com.aim.server.domain.address.repository.addressManagement

import com.aim.server.domain.address.entity.AddressManagement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
interface AddressManagementRepository: JpaRepository<AddressManagement, Long>, AddressManagementQueryRepository {
}
