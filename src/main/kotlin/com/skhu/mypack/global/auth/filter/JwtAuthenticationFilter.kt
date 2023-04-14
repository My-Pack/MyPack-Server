package com.skhu.mypack.global.auth.filter

import com.skhu.mypack.global.auth.app.JwtProvider
import com.skhu.mypack.global.util.HeaderUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
        private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val accessToken = HeaderUtil.getAccessToken(request)

        if (accessToken != null) {
            val isTokenValid = jwtProvider.isValidToken(accessToken)
            val isTokenExpired = jwtProvider.isExpiredToken(accessToken)
            val isSignUp = jwtProvider.isSignUp(accessToken)

            if (isTokenValid && !isTokenExpired && isSignUp) {
                val authentication = jwtProvider.getAuthentication(accessToken)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }
}