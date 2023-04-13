package com.skhu.mypack.auth.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class CantResolveAccessTokenException : CustomAbstractException(
    HttpStatus.BAD_REQUEST,
    "CANT_RESOLVE_ACCESS_TOKEN",
    "액세스 토큰을 새로 발급하는 과정에서 문제가 생겼습니다."
)