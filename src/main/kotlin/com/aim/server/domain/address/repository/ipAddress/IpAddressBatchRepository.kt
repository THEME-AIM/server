package com.aim.server.domain.address.repository.ipAddress

import com.aim.server.domain.address.dto.IpAddressData.IpAddressWithFloor
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class IpAddressBatchRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    /**
     * IP 주소를 batch로 insert함.
     * @param ipAddressList: List<IpAddressWithFloor>: IP 주소와 층 정보를 담은 리스트
     */
    @Transactional
    fun batchInsert(ipAddressList: List<IpAddressWithFloor>) {
        val query =
            "INSERT INTO public.ip_address (ip_address, floor, is_assigned) VALUES (?, ?, ?)"
        jdbcTemplate.batchUpdate(
            query, ipAddressList, ipAddressList.size
        ) { ps, ipAddress ->
            ps.setString(1, ipAddress.ipAddress)
            ps.setInt(2, ipAddress.floor)
            ps.setBoolean(3, false)
        }
    }
}