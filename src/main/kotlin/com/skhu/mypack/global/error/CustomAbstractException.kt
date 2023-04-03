package com.skhu.mypack.global.error

import org.springframework.http.HttpStatus

abstract class CustomAbstractException(
    val status: HttpStatus,
    val code: String,
    override val message: String = "default message"
) : RuntimeException(message)