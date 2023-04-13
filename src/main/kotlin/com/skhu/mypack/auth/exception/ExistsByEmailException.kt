package com.skhu.mypack.auth.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class ExistsByEmailException(email: String) : CustomAbstractException(
    HttpStatus.BAD_REQUEST,
    "EXISTS_BY_EMAIL",
    "이미 존재하는 이메일(email: $email)입니다."
)