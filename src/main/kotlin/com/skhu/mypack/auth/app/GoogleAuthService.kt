package com.skhu.mypack.auth.app

import com.skhu.mypack.auth.dto.request.GoogleCredentialRequest
import com.skhu.mypack.auth.dto.response.GoogleIdTokenPayloadResponse
import com.skhu.mypack.auth.exception.GoogleOAuth2ServerException
import com.skhu.mypack.auth.exception.InvalidGoogleClientIdException
import com.skhu.mypack.auth.exception.NoInfoGoogleIdTokenPayloadException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class GoogleAuthService(
        @Value("\${auth.google.client_id}")
        val clientIds: HashSet<String>,
        private val restTemplate: RestTemplate
) {

    fun getEmail(googleCredentialRequest: GoogleCredentialRequest): String {
        if (!isValidClientId(googleCredentialRequest.clientId)) {
            throw InvalidGoogleClientIdException()
        }

        val uri = UriComponentsBuilder
                .fromUriString("https://oauth2.googleapis.com")
                .path("/tokeninfo")
                .queryParam("id_token", googleCredentialRequest.idToken)
                .encode()
                .build()
                .toUri()

        val googleIdTokenPayloadResponse: GoogleIdTokenPayloadResponse = try {
            restTemplate.getForObject(uri, GoogleIdTokenPayloadResponse::class.java)
                    ?: throw NoInfoGoogleIdTokenPayloadException()
        } catch (e: Exception) {
            throw GoogleOAuth2ServerException()
        }

        if (!isValidClientId(googleIdTokenPayloadResponse.clientId)) {
            throw InvalidGoogleClientIdException()
        }

        return googleIdTokenPayloadResponse.email
    }

    private fun isValidClientId(clientId: String): Boolean {
        return clientIds.contains(clientId)
    }
}