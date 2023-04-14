package com.skhu.mypack.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class TokenRequest(
    @NotBlank
    val accessToken: String,
    @NotBlank
    val refreshToken: String,
)
