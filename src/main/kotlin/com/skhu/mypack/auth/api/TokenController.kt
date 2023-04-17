package com.skhu.mypack.auth.api

import com.skhu.mypack.auth.app.TokenService
import com.skhu.mypack.auth.dto.request.TokenRequest
import com.skhu.mypack.auth.dto.response.TokenResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
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

    @Operation(
        summary = "액세스 토큰 갱신",
        description = "만료된 액세스 토큰을 갱신합니다.\n 만료되지 않은 리프레시 토큰을 통해서만 갱신이 가능합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "액세스 토큰 갱신 성공"),
            ApiResponse(
                responseCode = "400",
                description = "액세스 토큰 혹은 리프래쉬 토큰이 검증되지않음, 액세스 토큰이 만료 상태가 아님, 리프래쉬 토큰이 만료됨",
                content = [Content()]
            ),
        ],
    )
    @PostMapping("/refresh")
    fun refresh(@RequestBody tokenRequest: TokenRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(tokenService.resolveAccessToken(tokenRequest))
    }
}