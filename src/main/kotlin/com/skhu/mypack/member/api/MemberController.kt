package com.skhu.mypack.member.api

import com.skhu.mypack.global.auth.PrincipalDetails
import com.skhu.mypack.member.app.MemberService
import com.skhu.mypack.member.dto.request.MemberUpdateRequest
import com.skhu.mypack.member.dto.response.MemberResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService,
) {
    @GetMapping
    fun findAllByName(@RequestParam name: String, pageable: Pageable): ResponseEntity<Slice<MemberResponse>> {
        return ResponseEntity.ok(memberService.findAllByName(name, pageable))
    }

    @GetMapping("/{name}")
    fun findByName(@PathVariable name: String): ResponseEntity<MemberResponse> {
        return ResponseEntity.ok(memberService.findByName(name))
    }

    @PatchMapping
    fun updateByName(
        @RequestBody memberUpdateRequest: MemberUpdateRequest,
        @AuthenticationPrincipal principal: PrincipalDetails
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity.ok(memberService.updateName(principal, memberUpdateRequest))
    }
}