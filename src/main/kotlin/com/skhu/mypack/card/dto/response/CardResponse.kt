package com.skhu.mypack.card.dto.response

import com.skhu.mypack.card.domain.Card
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "카드 응답 DTO")
data class CardResponse(
    @Schema(description = "카드 ID", example = "1")
    val id: Long,
    @Schema(description = "카드 제목", example = "제목")
    val title: String,
    @Schema(description = "카드 내용", example = "내용")
    val content: String,
    @Schema(description = "카드 이미지 URL", example = "http://localhost:8080/api/v1/images/1")
    val imageUrl: String,
    @Schema(description = "카드 색상", example = "red")
    val color: String,
    @Schema(description = "카드 테마", example = "christmas")
    val theme: String,
    @Schema(description = "카드 좋아요 개수", example = "0")
    val likeCount: Int,
    @Schema(description = "카드 댓글 개수", example = "0")
    val commentCount: Int,
    @Schema(description = "카드 작성자", example = "gustn1234")
    val memberName: String,
    @Schema(description = "카드 생성일", example = "2021-08-01T00:00:00")
    val createdAt: LocalDateTime,
    @Schema(description = "카드 수정일", example = "2021-08-01T00:00:00")
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun of(card: Card): CardResponse {
            return CardResponse(
                id = card.id!!,
                title = card.title,
                content = card.content,
                imageUrl = card.image.s3Url,
                color = card.color,
                theme = card.theme,
                likeCount = card.likeCount,
                commentCount = card.commentCount,
                memberName = card.member.name,
                createdAt = card.createdAt!!,
                updatedAt = card.modifiedDate!!,
            )
        }
    }
}


