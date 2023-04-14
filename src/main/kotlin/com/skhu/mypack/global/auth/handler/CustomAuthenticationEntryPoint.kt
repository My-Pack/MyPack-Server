package com.skhu.mypack.global.auth.handler

import com.skhu.mypack.global.auth.PrincipalDetails
import com.skhu.mypack.global.auth.app.JwtProvider
import com.skhu.mypack.global.util.HeaderUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint(
        private val jwtProvider: JwtProvider
) : AuthenticationEntryPoint {
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException?) {
        val accessToken: String? = HeaderUtil.getAccessToken(request)
        var status = 401
        var code = "UNAUTHORIZED"
        var message = "인증에서 문제가 발생 했습니다."

        if (accessToken == null) {
            code = "ACCESS_TOKEN_IS_NULL"
            message = "액세스 토큰이 존재하지 않습니다."
        } else if (!jwtProvider.isValidToken(accessToken)) {
            code = "ACCESS_TOKEN_IS_INVALID"
            message = "액세스 토큰이 유효하지 않습니다."
        } else if (jwtProvider.isExpiredToken(accessToken)) {
            status = 403
            code = "ACCESS_TOKEN_IS_EXPIRED"
            message = "액세스 토큰이 만료되었습니다."
        } else if (!jwtProvider.isSignUp(accessToken)) {
            status = 400
            code = "NEED_SIGN_UP"
            message = "회원가입이 필요합니다."
        }

        response.contentType = "application/json;charset=UTF-8"
        response.status = status
        response.writer.println(
                """
                {
                    "status": $status,
                    "code": "$code",
                    "message": "$message"
                }
                """.trimIndent()
        )
    }
}