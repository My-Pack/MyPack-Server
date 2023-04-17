package com.skhu.mypack.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "수정하기 위한 회원 정보")
data class MemberUpdateRequest(
    @Schema(description = "기존 이름(현재 로그인한 유저와 같아야함)", example = "홍길동")
    @NotBlank
    val oldName: String,
    @Schema(description = "새로운 이름", example = "롱길동")
    @NotBlank
    val newName: String,
)
