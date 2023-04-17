package com.skhu.mypack.global.auth.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDinedHandler : AccessDeniedHandler {
    override fun handle(request: HttpServletRequest?, response: HttpServletResponse, accessDeniedException: AccessDeniedException?) {
        response.contentType = "application/json;charset=UTF-8"
        response.status = 403
        response.writer.println(
                """
                {
                    "status": ${403},
                    "code": "NO_PERMISSION",
                    "message": "권한이 없습니다."
                }
                """.trimIndent()
        )
    }
}