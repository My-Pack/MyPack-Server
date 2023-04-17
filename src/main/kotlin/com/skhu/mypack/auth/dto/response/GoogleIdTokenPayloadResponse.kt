package com.skhu.mypack.auth.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class GoogleIdTokenPayloadResponse(
    @JsonProperty("aud")
    val clientId: String,
    val email: String,
)
