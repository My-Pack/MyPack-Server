package com.skhu.mypack.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class CommonConfig {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()
}