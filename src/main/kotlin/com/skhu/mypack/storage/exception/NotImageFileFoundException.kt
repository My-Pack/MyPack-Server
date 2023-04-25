package com.skhu.mypack.storage.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class NotImageFileFoundException(id: Long) : CustomAbstractException(
    HttpStatus.BAD_REQUEST,
    "NOT_IMAGE_FILE_FOUND",
    "이미지 파일을 찾을 수 없습니다. (id: $id)"
)