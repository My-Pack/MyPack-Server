package com.skhu.mypack.auth.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "구글 로그인 정보")
data class GoogleCredentialRequest(
    @Schema(description = "구글 ID 토큰", example = "id_token")
    @JsonProperty("credential")
    @NotBlank
    var idToken: String,
    @Schema(description = "클라이언트 ID", example = "client_id")
    @NotBlank
    var clientId: String,
)
