package com.aim.server.core.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.spring.SpringExtension

class KotestSpringConfig : AbstractProjectConfig() {
    override fun extensions() = listOf(SpringExtension)
}