package com.skhu.mypack.auth.app

import com.skhu.mypack.auth.dao.RefreshTokenRepository
import com.skhu.mypack.auth.dto.response.TokenResponse
import com.skhu.mypack.auth.exception.CantResolveAccessTokenException
import com.skhu.mypack.global.auth.PrincipalDetails
import com.skhu.mypack.global.auth.app.JwtProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TokenService(
    private val jwtProvider: JwtProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    @Transactional
    fun createTokens(principalDetails: PrincipalDetails): TokenResponse {
        val auth = principalDetails.toAuthentication()
        val accessToken = jwtProvider.createAccessToken(auth)
        val refreshToken = jwtProvider.createRefreshToken()

        refreshTokenRepository.save(refreshToken, principalDetails.email)

        return TokenResponse(accessToken, refreshToken)
    }

    @Transactional(readOnly = true)
    fun resolveAccessToken(accessToken: String, refreshToken: String): TokenResponse {
        val validTokens = { access: String, refresh: String ->
            jwtProvider.isValidToken(access) && jwtProvider.isValidToken(refresh)
        }

        val canResolveAccessToken = { access: String, refresh: String ->
            jwtProvider.isExpiredToken(access) && !jwtProvider.isExpiredToken(refresh)
        }

        if (validTokens(accessToken, refreshToken) && canResolveAccessToken(accessToken, refreshToken)) {
            val auth = jwtProvider.getAuthentication(accessToken)
            val email = auth.name

            if (refreshTokenRepository.findEmailByToken(refreshToken) == email) {
                val newAccessToken = jwtProvider.createAccessToken(auth)

                return TokenResponse(newAccessToken, refreshToken)
            }
        }

        throw CantResolveAccessTokenException()
    }
}