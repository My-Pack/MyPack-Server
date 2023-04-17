package com.skhu.mypack.auth.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "토큰 정보")
data class TokenRequest(
    @Schema(description = "만료됬고 검증된 액세스 토큰", example = "access_token")
    @NotBlank
    val accessToken: String,
    @Schema(description = "만료되지 않았고 검증된 리프레시 토큰", example = "refresh_token")
    @NotBlank
    val refreshToken: String,
)
