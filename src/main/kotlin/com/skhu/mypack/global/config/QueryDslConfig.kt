package com.skhu.mypack.global.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDslConfig(private val entityManager: EntityManager) {
    @Bean
    fun queryDsl(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}