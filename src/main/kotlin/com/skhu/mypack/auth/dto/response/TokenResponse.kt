package com.skhu.mypack.auth.dto.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
