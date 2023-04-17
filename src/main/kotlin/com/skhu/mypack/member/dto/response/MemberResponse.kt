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
) {
    companion object {
        fun of(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                email = member.email,
                name = member.name,
            )
        }
    }
}
