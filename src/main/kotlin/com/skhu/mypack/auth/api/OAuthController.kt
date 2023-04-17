package com.skhu.mypack.auth.api

import com.skhu.mypack.auth.app.GoogleAuthService
import com.skhu.mypack.auth.app.TokenService
import com.skhu.mypack.auth.dto.request.GoogleCredentialRequest
import com.skhu.mypack.auth.dto.response.TokenResponse
import com.skhu.mypack.member.app.MemberService
import com.skhu.mypack.member.domain.enum.Provider
import com.skhu.mypack.member.domain.enum.Role
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/auth")
class OAuthController(
    private val googleAuthService: GoogleAuthService,
    private val memberService: MemberService,
    private val tokenService: TokenService,
) {

    @PostMapping("/google")
    fun googleLogin(@RequestBody googleCredentialRequest: GoogleCredentialRequest): ResponseEntity<TokenResponse> {
        val googleEmail = googleAuthService.getEmail(googleCredentialRequest)
        val principal = memberService.saveOrFindMember(googleEmail, Role.ROLE_USER, Provider.GOOGLE)

        return ResponseEntity.ok(tokenService.createTokens(principal))
    }
}