package com.skhu.mypack.global.config

import com.skhu.mypack.global.auth.filter.JwtAuthenticationFilter
import com.skhu.mypack.global.auth.handler.CustomAccessDinedHandler
import com.skhu.mypack.global.auth.handler.CustomAuthenticationEntryPoint
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.CorsUtils
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
        private val jwtAuthenticationFilter: JwtAuthenticationFilter,
        private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
        private val customAccessDinedHandler: CustomAccessDinedHandler,
        @Value("\${cors.allowed-origins}")
        private val corsOrigins: Array<String>,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        return http
                .cors {
                    it.configurationSource(configurationSource())
                }
                .csrf {
                    it.disable()
                }
                .formLogin {
                    it.disable()
                }
                .httpBasic {
                    it.disable()
                }
                .sessionManagement {
                    it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                }
                .authorizeHttpRequests {
                    it.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers("api/v1/auth/ping-user").hasRole("USER")
                        .requestMatchers("api/v1/auth/ping-user").hasRole("USER")
                            .anyRequest().permitAll()
                }
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
                .exceptionHandling {
                    it.authenticationEntryPoint(customAuthenticationEntryPoint)
                            .accessDeniedHandler(customAccessDinedHandler)
                }
                .build()
    }

    @Bean
    fun configurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.setAllowedOriginPatterns(corsOrigins.toMutableList())
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.exposedHeaders = listOf("Access-Control-Allow-Credentials", "Authorization", "Set-Cookie")
        configuration.allowCredentials = true
        configuration.maxAge = 3600L
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}