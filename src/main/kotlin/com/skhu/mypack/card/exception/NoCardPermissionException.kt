package com.skhu.mypack.card.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class NoCardPermissionException : CustomAbstractException(HttpStatus.FORBIDDEN, "NO_CARD_PERMISSION", "해당 카드에 대한 권한이 없습니다.")