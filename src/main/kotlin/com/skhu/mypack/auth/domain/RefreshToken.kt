package com.skhu.mypack.auth.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "refreshToken", timeToLive = 60 * 120)
class RefreshToken(
    @Id
    val token: String,
    val email: String,
)