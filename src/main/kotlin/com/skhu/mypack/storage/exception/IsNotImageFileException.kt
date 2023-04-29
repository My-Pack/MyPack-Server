package com.skhu.mypack.storage.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class IsNotImageFileException : CustomAbstractException(HttpStatus.BAD_REQUEST, "IS_NOT_IMAGE_FILE", "이미지 파일이 아닙니다.") {
}