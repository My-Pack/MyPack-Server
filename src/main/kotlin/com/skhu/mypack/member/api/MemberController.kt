package com.skhu.mypack.member.api

import com.skhu.mypack.global.auth.PrincipalDetails
import com.skhu.mypack.member.app.MemberService
import com.skhu.mypack.member.dto.request.MemberUpdateRequest
import com.skhu.mypack.member.dto.response.MemberResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.converters.models.PageableAsQueryParam
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
    @Operation(
        summary = "회원 이름으로 조회",
        description = "회원 이름으로 회원들을 조회합니다. 일부분만 검색해도 해당하는 멤버들을 반환합니다.(ex - 홍으로 검색하면 홍길동이나옴)",
        parameters = [
            Parameter(name = "name", description = "찾고싶은 이름", example = "홍길동"),
        ],
        responses = [
            ApiResponse(responseCode = "200", description = "회원 조회 성공"),
        ],
    )
    @PageableAsQueryParam
    @GetMapping
    fun findAllByName(
        @RequestParam name: String,
        @Parameter(hidden = true) pageable: Pageable
    ): ResponseEntity<Slice<MemberResponse>> {
        return ResponseEntity.ok(memberService.findAllByName(name, pageable))
    }

    @Operation(
        summary = "회원 이름으로 조회",
        description = "회원 이름으로 회원을 조회합니다.",
        parameters = [
            Parameter(name = "name", description = "이름", example = "홍길동"),
        ],
        responses = [
            ApiResponse(responseCode = "200", description = "회원 조회 성공"),
            ApiResponse(responseCode = "404", description = "회원 정보 없음", content = [Content()])
        ],
    )
    @GetMapping("/{name}")
    fun findByName(@PathVariable name: String): ResponseEntity<MemberResponse> {
        return ResponseEntity.ok(memberService.findByName(name))
    }

    @Operation(
        summary = "회원 수정",
        description = "회원을 수정합니다. 현재 로그인된(JWT 토큰)과 request의 oldName이 같아야 작동합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "회원 수정 성공"),
            ApiResponse(responseCode = "403", description = "수정 권한 없음", content = [Content()]),
            ApiResponse(responseCode = "404", description = "회원 정보 없음", content = [Content()])
        ],
    )
    @PatchMapping
    fun update(
        @RequestBody memberUpdateRequest: MemberUpdateRequest,
        @AuthenticationPrincipal principal: PrincipalDetails
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity.ok(memberService.update(principal, memberUpdateRequest))
    }
}