package com.skhu.mypack.member.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class MemberNotFoundException(email: String) :
        CustomAbstractException(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "해당 요소(${email})로 멤버를 찾을 수 없음")