package com.skhu.mypack.member.dto.request

import jakarta.validation.constraints.NotBlank

data class MemberUpdateRequest(
    @NotBlank
    val oldName: String,
    @NotBlank
    val newName: String,
)
