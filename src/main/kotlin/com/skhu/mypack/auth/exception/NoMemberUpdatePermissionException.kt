package com.skhu.mypack.auth.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class NoMemberUpdatePermissionException :
    CustomAbstractException(HttpStatus.FORBIDDEN, "NO_MEMBER_UPDATE_PERMISSION", "회원 정보 수정 권한이 없습니다.") {
}