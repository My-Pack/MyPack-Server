package com.skhu.mypack.app

import com.skhu.mypack.auth.app.TokenService
import com.skhu.mypack.auth.dao.RefreshTokenRepository
import com.skhu.mypack.auth.dto.request.TokenRequest
import com.skhu.mypack.auth.exception.CantResolveAccessTokenException
import com.skhu.mypack.global.auth.PrincipalDetails
import com.skhu.mypack.global.auth.app.JwtProvider
import com.skhu.mypack.member.domain.enum.Role
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TokenServiceTests {

    @MockK
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @MockK
    private lateinit var jwtProvider: JwtProvider

    @InjectMockKs
    private lateinit var tokenService: TokenService

    @BeforeAll
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Nested
    inner class CreateTokens {
        @Nested
        inner class Success {
            @Test
            fun `토큰을 정상적으로 생성하는 경우`() {
                // given
                val principalDetails = PrincipalDetails("email", Role.ROLE_USER)
                every { jwtProvider.createAccessToken(any()) } returns "accessToken"
                every { jwtProvider.createRefreshToken() } returns "refreshToken"
                every { refreshTokenRepository.save(any(), any()) } returns Unit

                // when
                val tokenResponse = tokenService.createTokens(principalDetails)

                // then
                verifyOrder {
                    jwtProvider.createAccessToken(any())
                    jwtProvider.createRefreshToken()
                    refreshTokenRepository.save(any(), any())
                }
                assertSame("accessToken", tokenResponse.accessToken)
                assertSame("refreshToken", tokenResponse.refreshToken)
            }
        }
    }

    @Nested
    inner class ResolveAccessToken {
        @Nested
        inner class Success {
            @Test
            fun `액세스 토큰 리프래쉬를 정상적으로 한 경우`() {
                // given
                val tokenRequest = TokenRequest("validAccessToken", "validRefreshToken")
                val auth = PrincipalDetails("user1@gmail.com", Role.ROLE_USER).toAuthentication()

                every { jwtProvider.isValidToken(any()) } returns true
                every { jwtProvider.isExpiredToken(tokenRequest.accessToken) } returns true
                every { jwtProvider.isExpiredToken(tokenRequest.refreshToken) } returns false
                every { jwtProvider.getAuthentication(tokenRequest.accessToken) } returns auth
                every { refreshTokenRepository.findEmailByToken(tokenRequest.refreshToken) } returns "user1@gmail.com"
                every { jwtProvider.createAccessToken(auth) } returns "newAccessToken"

                // when
                val tokenResponse = tokenService.resolveAccessToken(tokenRequest)

                // then
                verifyOrder {
                    jwtProvider.isValidToken(tokenRequest.accessToken)
                    jwtProvider.isValidToken(tokenRequest.refreshToken)
                    jwtProvider.isExpiredToken(tokenRequest.accessToken)
                    jwtProvider.isExpiredToken(tokenRequest.refreshToken)
                    jwtProvider.getAuthentication(tokenRequest.accessToken)
                    refreshTokenRepository.findEmailByToken(tokenRequest.refreshToken)
                    jwtProvider.createAccessToken(auth)
                }
                assertSame("newAccessToken", tokenResponse.accessToken)
            }
        }

        @Nested
        inner class Fail {
            @Test
            fun `액세스 토큰 검증을 실패할 경우`() {
                // given
                val tokenRequest = TokenRequest("invalidAccessToken", "refreshToken")

                every { jwtProvider.isValidToken(tokenRequest.accessToken) } returns false

                // when then
                assertThrows<CantResolveAccessTokenException> { tokenService.resolveAccessToken(tokenRequest) }
                verify {
                    jwtProvider.isValidToken(tokenRequest.accessToken)
                }
            }

            @Test
            fun `리프래쉬 토큰 검증을 실패할 경우`() {
                // given
                val tokenRequest = TokenRequest("validAccessToken", "invalidRefreshToken")

                every { jwtProvider.isValidToken(tokenRequest.accessToken) } returns true
                every { jwtProvider.isValidToken(tokenRequest.refreshToken) } returns false

                // when then
                assertThrows<CantResolveAccessTokenException> { tokenService.resolveAccessToken(tokenRequest) }
                verifyOrder {
                    jwtProvider.isValidToken(tokenRequest.accessToken)
                    jwtProvider.isValidToken(tokenRequest.refreshToken)
                }
            }

            @Test
            fun `액세스 토큰이 만료되지 않았을 경우`() {
                // given
                val tokenRequest = TokenRequest("notExpiredAccessToken", "refreshToken")

                every { jwtProvider.isValidToken(any()) } returns true
                every { jwtProvider.isExpiredToken(tokenRequest.accessToken) } returns false

                // when then
                assertThrows<CantResolveAccessTokenException> { tokenService.resolveAccessToken(tokenRequest) }
                verifyOrder {
                    jwtProvider.isValidToken(tokenRequest.accessToken)
                    jwtProvider.isValidToken(tokenRequest.refreshToken)
                    jwtProvider.isExpiredToken(tokenRequest.accessToken)
                }
            }

            @Test
            fun `리프래쉬 토큰이 만료됬을 경우`() {
                // given
                val tokenRequest = TokenRequest("accessToken", "expiredRefreshToken")

                every { jwtProvider.isValidToken(any()) } returns true
                every { jwtProvider.isExpiredToken(tokenRequest.accessToken) } returns true
                every { jwtProvider.isExpiredToken(tokenRequest.refreshToken) } returns true

                // when then
                assertThrows<CantResolveAccessTokenException> { tokenService.resolveAccessToken(tokenRequest) }
                verifyOrder {
                    jwtProvider.isValidToken(tokenRequest.accessToken)
                    jwtProvider.isValidToken(tokenRequest.refreshToken)
                    jwtProvider.isExpiredToken(tokenRequest.accessToken)
                    jwtProvider.isExpiredToken(tokenRequest.refreshToken)
                }
            }

            @Test
            fun `리프래쉬 토큰에 연관된 이메일이 없거나 다를 경우`() {
                // given
                val tokenRequest = TokenRequest("accessToken", "refreshToken")
                val auth = PrincipalDetails("user1@gmail.com", Role.ROLE_USER).toAuthentication()

                every { jwtProvider.isValidToken(any()) } returns true
                every { jwtProvider.isExpiredToken(tokenRequest.accessToken) } returns true
                every { jwtProvider.isExpiredToken(tokenRequest.refreshToken) } returns false
                every { jwtProvider.getAuthentication(tokenRequest.accessToken) } returns auth
                every { refreshTokenRepository.findEmailByToken(tokenRequest.refreshToken) } returns "user2@gmail.com"

                // when then
                assertThrows<CantResolveAccessTokenException> { tokenService.resolveAccessToken(tokenRequest) }
                verifyOrder {
                    jwtProvider.isValidToken(tokenRequest.accessToken)
                    jwtProvider.isValidToken(tokenRequest.refreshToken)
                    jwtProvider.isExpiredToken(tokenRequest.accessToken)
                    jwtProvider.isExpiredToken(tokenRequest.refreshToken)
                    jwtProvider.getAuthentication(tokenRequest.accessToken)
                    refreshTokenRepository.findEmailByToken(tokenRequest.refreshToken)
                }
            }
        }
    }
}