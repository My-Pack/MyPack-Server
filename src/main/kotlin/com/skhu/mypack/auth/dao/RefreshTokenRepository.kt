package com.skhu.mypack.auth.dao

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RefreshTokenRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    fun save(refreshToken: String, email: String) {
        redisTemplate.opsForValue().set(refreshToken, email, 2, TimeUnit.HOURS)
    }

    fun findEmailByToken(refreshToken: String): String? {
        return redisTemplate.opsForValue().get(refreshToken)
    }
}