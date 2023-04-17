package com.skhu.mypack.auth.api

import com.skhu.mypack.auth.app.TokenService
import com.skhu.mypack.auth.dto.request.TokenRequest
import com.skhu.mypack.auth.dto.response.TokenResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class TokenController(
    private val tokenService: TokenService,
) {

    @PostMapping("/refresh")
    fun refresh(@RequestBody tokenRequest: TokenRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(tokenService.resolveAccessToken(tokenRequest))
    }
}