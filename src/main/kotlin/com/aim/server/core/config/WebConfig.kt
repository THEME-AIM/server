package com.aim.server.core.config

import com.aim.server.core.interceptor.AuthenticatedInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "DELETE", "PATCH")
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AuthenticatedInterceptor())
            .addPathPatterns("/api/**")
    }
}