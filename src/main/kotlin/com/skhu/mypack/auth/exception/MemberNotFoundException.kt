package com.skhu.mypack.auth.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class MemberNotFoundException(email: String) :
        CustomAbstractException(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "해당 이메일(${email})로 멤버를 찾을 수 없음")