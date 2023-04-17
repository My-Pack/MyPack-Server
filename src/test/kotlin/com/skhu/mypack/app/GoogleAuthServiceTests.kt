package com.skhu.mypack.app

import com.skhu.mypack.auth.app.GoogleAuthService
import com.skhu.mypack.auth.dto.request.GoogleCredentialRequest
import com.skhu.mypack.auth.dto.response.GoogleIdTokenPayloadResponse
import com.skhu.mypack.auth.exception.GoogleOAuth2ServerException
import com.skhu.mypack.auth.exception.InvalidGoogleClientIdException
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.client.RestTemplate
import java.net.URI

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GoogleAuthServiceTests {

    @MockK
    private lateinit var restTemplate: RestTemplate
    private lateinit var googleAuthService: GoogleAuthService

    @BeforeAll
    fun setUp() {
        MockKAnnotations.init(this)
        googleAuthService = GoogleAuthService(hashSetOf("valid.com"), restTemplate)
    }

    @Nested
    inner class GetEmail {
        @Nested
        inner class Success {
            @Test
            fun `Google OAuth2 서버에 정상적으로 정보를 가져온 경우`() {
                // given
                val googleCredentialRequest = GoogleCredentialRequest("validToken", "valid.com")
                val googleIdTokenPayloadResponse = GoogleIdTokenPayloadResponse("valid.com", "validEmail")
                every {
                    restTemplate.getForObject(
                        any<URI>(),
                        GoogleIdTokenPayloadResponse::class.java
                    )
                } returns googleIdTokenPayloadResponse

                // when
                val resultEmail = googleAuthService.getEmail(googleCredentialRequest)

                // then
                verify { restTemplate.getForObject(any<URI>(), GoogleIdTokenPayloadResponse::class.java) }
                assertEquals("validEmail", resultEmail)
            }
        }

        @Nested
        inner class Fail {
            @Test
            fun `요청한 클라이언트 ID가 OAuth2와 다를 경우`() {
                // given
                val googleCredentialRequest = GoogleCredentialRequest("validToken", "invalid.com")

                // when
                var exceptionThrown = false
                try {
                    googleAuthService.getEmail(googleCredentialRequest)
                } catch (e: InvalidGoogleClientIdException) {
                    exceptionThrown = true
                }

                // then
                assertTrue(exceptionThrown)
            }

            @Test
            fun `요청한 ID 토큰이 만료, 유효하지 않은 경우`() {
                // given
                val googleCredentialRequest = GoogleCredentialRequest("invalidToken", "valid.com")
                every {
                    restTemplate.getForObject(
                        any<URI>(),
                        GoogleIdTokenPayloadResponse::class.java
                    )
                } throws Exception()

                // when
                var exceptionThrown = false
                try {
                    googleAuthService.getEmail(googleCredentialRequest)
                } catch (e: GoogleOAuth2ServerException) {
                    exceptionThrown = true
                }

                // then
                assertTrue(exceptionThrown)
            }
        }
    }
}