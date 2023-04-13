package com.skhu.mypack.global.util

import jakarta.servlet.http.HttpServletRequest


private const val HEADER_AUTHORIZATION = "Authorization"
private const val TOKEN_PREFIX = "Bearer "

class HeaderUtil {
    companion object {
        fun getAccessToken(request: HttpServletRequest): String? {
            val headerValue = request.getHeader(HEADER_AUTHORIZATION) ?: return null
            return if (headerValue.startsWith(TOKEN_PREFIX)) {
                headerValue.substring(TOKEN_PREFIX.length)
            } else null
        }
    }
}