package com.skhu.mypack.auth.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class GoogleOAuth2ServerException : CustomAbstractException(
    HttpStatus.BAD_REQUEST,
    "GOOGLE_OAUTH2_SERVER_ERROR",
    "구글 OAuth2 서버에 오류가 발생했습니다."
)