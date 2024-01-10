package com.aim.server.domain.address.service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OpenStackNetworkServiceImplTest(
    private val openStackNetworkService: OpenStackNetworkService
) {
    @Test
    fun createNetwork() {
        openStackNetworkService.createNetwork("test", "192.168.0")
    }
}