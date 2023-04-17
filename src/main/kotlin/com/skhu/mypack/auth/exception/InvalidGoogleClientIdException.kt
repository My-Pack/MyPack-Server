package com.skhu.mypack.auth.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class InvalidGoogleClientIdException : CustomAbstractException(
    HttpStatus.BAD_REQUEST,
    "INVALID_GOOGLE_CLIENT_ID",
    "요청된 구글 클라이언트 ID가 서버에 등록된 클라이언트 ID와 일치하지 않습니다."
)