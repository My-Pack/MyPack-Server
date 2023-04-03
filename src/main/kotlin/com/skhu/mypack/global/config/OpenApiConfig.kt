package com.skhu.mypack.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val info = Info()
            .title("MyPack API Document")
            .version("v0.0.1")
            .description("MyPack 문서")
            .contact(Contact().name("My Pack").url("https://github.com/My-Pack"))
            .license(
                License().name("MIT License").url("https://github.com/My-Pack/MyPack-Server/blob/main/LICENSE")
            )

        val authName = "JWT Access Token"
        val securityRequirement = SecurityRequirement().addList(authName)
        val components = Components()
            .addSecuritySchemes(
                authName,
                SecurityScheme()
                    .name(authName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("Bearer")
                    .bearerFormat("JWT")
                    .description("액세스 토큰을 입력해주세요.(Bearer 안붙여도됨)")
            )

        return OpenAPI()
            .addSecurityItem(securityRequirement)
            .components(components)
            .info(info)
    }
}