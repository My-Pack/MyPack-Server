package com.skhu.mypack.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.CorsUtils
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    @Value("\${cors.allowed-origins}")
    val corsOrigins: Array<String>
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // TODO: invoke를 이용해서 DSL 구성
//        http.invoke {
//        }

        return http
            .cors().configurationSource(configurationSource())
            .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//            .requestMatchers("/swagger-ui/**").permitAll()
//            .requestMatchers("/api-docs/**").permitAll()
//            .anyRequest().authenticated()
            // TODO: api 구성 후 request 권한 검증 추가하기
            .anyRequest().permitAll()
            .and()
            // TODO: Add filter, authenticationEntryPoint, accessDeniedHandler
//            .addFilterBefore(?, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling()
//            .authenticationEntryPoint(authenticationEntryPoint())
//            .accessDeniedHandler(accessDeniedHandler())
            .and()
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