package com.skhu.mypack.global.auth.app

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

private const val ACCESS_TOKEN_EXPIRED_TIME = 1000 * 60 * 10
private const val REFRESH_TOKEN_EXPIRED_TIME = 1000 * 60 * 120

@Component
class JwtProvider(
    private val principalService: PrincipalService,
    @Value("\${auth.jwt.secret}")
    private val secretStr: String,
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(secretStr.toByteArray(StandardCharsets.UTF_8))

    fun createAccessToken(authentication: Authentication): String {
        val now = Date()

        return Jwts.builder()
            .setSubject(authentication.name)
            .claim("roles", authentication.authorities)
            .setExpiration(Date(now.time + ACCESS_TOKEN_EXPIRED_TIME))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun createRefreshToken(): String {
        val now = Date()

        return Jwts.builder()
            .setSubject(UUID.randomUUID().toString())
            .setExpiration(Date(now.time + REFRESH_TOKEN_EXPIRED_TIME))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun getAuthentication(accessToken: String): Authentication {
        val principal = principalService.loadUserByUsername(getUsernameFromAccessToken(accessToken))
        return UsernamePasswordAuthenticationToken(principal, "", principal.authorities)
    }

    fun getUsernameFromAccessToken(accessToken: String): String {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accessToken)
                .body
                .subject
        } catch (e: ExpiredJwtException) {
            e.claims.subject
        }
    }

    fun isValidToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            true
        } catch (expiredTokenException: ExpiredJwtException) {
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun isExpiredToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            false
        } catch (expiredTokenException: ExpiredJwtException) {
            true
        } catch (e: Exception) {
            false
        }
    }
}