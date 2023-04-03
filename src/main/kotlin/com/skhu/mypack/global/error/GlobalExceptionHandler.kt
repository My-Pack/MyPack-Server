package com.skhu.mypack.global.error

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomAbstractException::class)
    fun handleCustomAbstractException(e: CustomAbstractException): ResponseEntity<ErrorResponse> {
        return ErrorResponse(e.status.value(), e.code, e.message).toResponseEntity(e)
    }
}