package com.skhu.mypack.auth.api

import com.skhu.mypack.auth.app.GoogleAuthService
import com.skhu.mypack.auth.app.TokenService
import com.skhu.mypack.auth.dto.request.GoogleCredentialRequest
import com.skhu.mypack.auth.dto.response.TokenResponse
import com.skhu.mypack.member.app.MemberService
import com.skhu.mypack.member.domain.enum.Provider
import com.skhu.mypack.member.domain.enum.Role
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/auth")
class OAuthController(
    private val googleAuthService: GoogleAuthService,
    private val memberService: MemberService,
    private val tokenService: TokenService,
) {

    @Operation(
        summary = "구글 로그인",
        description = "구글 로그인을 통해서 JWT 토큰을 발급받습니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "로그인 성공"),
            ApiResponse(
                responseCode = "404",
                description = "client id가 다름, Google OAuth2 서버 오류",
                content = [Content()]
            ),
        ]
    )
    @PostMapping("/google")
    fun googleLogin(@RequestBody googleCredentialRequest: GoogleCredentialRequest): ResponseEntity<TokenResponse> {
        val googleEmail = googleAuthService.getEmail(googleCredentialRequest)
        val principal = memberService.saveOrFindMember(googleEmail, Role.ROLE_USER, Provider.GOOGLE)

        return ResponseEntity.ok(tokenService.createTokens(principal))
    }
}