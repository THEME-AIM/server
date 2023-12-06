package com.aim.server.domain.address.repository.ipAddress

import com.aim.server.domain.address.entity.IpAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
interface IpAddressRepository : JpaRepository<IpAddress, Long>, IpAddressQueryRepository {
//    @Modifying(clearAutomatically = true)
//    @Query("UPDATE IpAddress i SET i.floor = :floor WHERE i.ipAddress IN :ipAddresses")
//    fun updateIpAddressFloor(@Param("floor") floor: Int, @Param("ipAddresses") ipAddresses: List<String>)
}
