package com.skhu.mypack.auth.dto.response

import com.skhu.mypack.auth.domain.Member

data class MemberResponse(
    val id: Long,
    val email: String,
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
