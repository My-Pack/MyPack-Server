package com.skhu.mypack.card.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class CardNotFoundException(id: Long) : CustomAbstractException(HttpStatus.NOT_FOUND, "CARD_NOT_FOUND", "해당 ID($id)로 카드를 찾을 수 없습니다.")