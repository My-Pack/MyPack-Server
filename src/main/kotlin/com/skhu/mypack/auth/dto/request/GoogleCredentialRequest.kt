package com.skhu.mypack.auth.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class GoogleCredentialRequest(
    @JsonProperty("credential")
    @NotBlank
    var idToken: String,
    @NotBlank
    var clientId: String,
)
