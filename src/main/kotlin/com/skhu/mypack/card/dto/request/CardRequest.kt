package com.skhu.mypack.card.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(description = "카드 요청 DTO")
data class CardRequest(
    @Schema(description = "카드 ID(수정할 때만 사용)")
    val id: Long?,
    @Schema(description = "카드 제목", example = "제목")
    @NotBlank
    val title: String,
    @Schema(description = "카드 내용", example = "내용")
    @NotBlank
    val content: String,
    @Schema(description = "카드 이미지 ID", example = "1")
    @NotNull
    val imageId: Long,
    @Schema(description = "카드 색상", example = "red")
    val color: String,
    @Schema(description = "카드 테마", example = "christmas")
    val theme: String,
)