package com.aim.server.domain.openstack.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OpenStackAPIServiceImplTest {
    @Autowired
    lateinit var openStackAPIService: OpenStackAPIService

    @Test
    fun `getNovaList`() {
        println(openStackAPIService.getNovaList())
    }
}