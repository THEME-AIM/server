package com.aim.server.core.client

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openstack4j.api.OSClient.OSClientV3
import org.openstack4j.api.exceptions.AuthenticationException
import org.openstack4j.model.common.Identifier
import org.openstack4j.openstack.OSFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class AIMClient {
    private val log = KotlinLogging.logger { }

    @Value("\${openstack.keystone.endpoint}")
    private lateinit var endpoint: String

    @Value("\${openstack.keystone.username}")
    private lateinit var username: String

    @Value("\${openstack.keystone.password}")
    private lateinit var password: String

    @Value("\${openstack.keystone.domain}")
    private lateinit var domain: String

    @Bean
    fun osAuthToken(): OSClientV3 {
        try {
            OSFactory.enableHttpLoggingFilter(true)
            return OSFactory.builderV3()
                .endpoint(endpoint)
                .credentials(username, password, Identifier.byId(domain))
                .scopeToProject(Identifier.byName(username), Identifier.byId(domain))
                .authenticate()
        } catch (e: AuthenticationException) {
            log.error { "AuthenticationException: ${e.message}" }
            e.printStackTrace()
        }
        throw Exception("AuthenticationException")
    }
}