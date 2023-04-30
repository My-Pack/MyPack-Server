package com.skhu.mypack.member.dto.response

import com.skhu.mypack.member.domain.Member
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원 정보")
data class MemberResponse(
    @Schema(description = "ID", example = "1")
    val id: Long,
    @Schema(description = "이메일", example = "example@gmail.com")
    val email: String,
    @Schema(description = "이름", example = "홍길동")
    val name: String,
    @Schema(description = "프로필 이미지 url", example = "https://example.com/profile.jpg")
    val profileImageUrl: String? = null,
    @Schema(description = "배경 이미지 url", example = "https://example.com/background.jpg")
    val backgroundImageUrl: String? = null,
) {
    companion object {
        fun of(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                email = member.email,
                name = member.name,
                profileImageUrl = member.profileImage?.s3Url,
                backgroundImageUrl = member.backgroundImage?.s3Url,
            )
        }
    }
}
