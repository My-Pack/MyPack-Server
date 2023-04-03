package com.skhu.mypack.global.error

import org.springframework.http.ResponseEntity

data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String
)

fun ErrorResponse.toResponseEntity(exception: CustomAbstractException): ResponseEntity<ErrorResponse> =
    ResponseEntity.status(exception.status).body(this)