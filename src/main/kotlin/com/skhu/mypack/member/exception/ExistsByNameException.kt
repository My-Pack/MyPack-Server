package com.skhu.mypack.member.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class ExistsByNameException(name: String) : CustomAbstractException(
    HttpStatus.BAD_REQUEST,
    "EXISTS_BY_NAME",
    "이미 존재하는 이름(email: $name)입니다."
)