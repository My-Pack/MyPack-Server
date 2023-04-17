package com.skhu.mypack.auth.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class NoInfoGoogleIdTokenPayloadException : CustomAbstractException(
    HttpStatus.BAD_REQUEST,
    "NO_INFO_GOOGLE_ID_TOKEN_PAYLOAD",
    "구글 ID 토큰 페이로드에 정보가 없습니다."
)