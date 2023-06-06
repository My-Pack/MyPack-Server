package com.skhu.mypack.card.api

import com.skhu.mypack.card.app.CardService
import com.skhu.mypack.card.dto.request.CardRequest
import com.skhu.mypack.card.dto.response.CardResponse
import com.skhu.mypack.global.auth.PrincipalDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import org.springdoc.core.converters.models.PageableAsQueryParam
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/cards")
class CardController(
        private val cardService: CardService,
) {
    @Operation(
            summary = "카드 생성",
            description = "카드를 생성합니다. id를 넣지 말아주세요.",
            responses = [
                ApiResponse(responseCode = "201", description = "카드 생성 성공"),
                ApiResponse(responseCode = "400", description = "request의 value가 형식이 잘못됨", content = [Content()]),
            ]
    )
    @PostMapping
    fun save(
            @Valid
            @RequestBody
            cardRequest: CardRequest,
            @AuthenticationPrincipal
            principalDetails: PrincipalDetails,
    ): ResponseEntity<CardResponse> {
        val cardResponse = cardService.create(cardRequest, principalDetails)
        val uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cardResponse.id)
                .toUri()
        return ResponseEntity.created(uri).body(cardResponse)
    }

    @Operation(
            summary = "카드들 정보 조회",
            description = "카드들 정보를 조회합니다.",
            parameters = [
                Parameter(name = "title", description = "카드 제목(포함으로 검색)"),
                Parameter(name = "content", description = "카드 내용(포함으로 검색)"),
                Parameter(name = "color", description = "카드 색상(정확하게 검색)"),
                Parameter(name = "theme", description = "카드 테마(정확하게 검색)"),
                Parameter(name = "memberName", description = "카드 멤버 이름(정확하게 검색)"),
            ],
            responses = [
                ApiResponse(responseCode = "200", description = "카드들 조회 성공"),
                ApiResponse(responseCode = "204", description = "정보 없음", content = [Content()]),
            ],
    )
    @PageableAsQueryParam
    @GetMapping
    fun findAll(
            @RequestParam(required = false)
            title: String?,
            @RequestParam(required = false)
            content: String?,
            @RequestParam(required = false)
            color: String?,
            @RequestParam(required = false)
            theme: String?,
            @RequestParam(required = false)
            memberName: String?,
            @Parameter(hidden = true)
            pageable: Pageable,
    ): ResponseEntity<Slice<CardResponse>> {
        val cardResponses = cardService.findAll(title, content, color, theme, memberName, pageable)

        if (cardResponses.isEmpty) {
            return ResponseEntity.noContent().build()
        }

        return ResponseEntity.ok(cardResponses)
    }

    @Operation(
            summary = "카드 정보 조회",
            description = "ID를 이용해서 해당 카드 정보를 조회 합니다.",
            parameters = [
                Parameter(name = "id", description = "카드 ID"),
            ],
            responses = [
                ApiResponse(responseCode = "200", description = "카드 조회 성공"),
                ApiResponse(responseCode = "404", description = "정보를 찾을 수 없음", content = [Content()]),
            ],
    )
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CardResponse> {
        val cardResponse = cardService.findById(id)
        return ResponseEntity.ok(cardResponse)
    }

    @Operation(
            summary = "카드 정보 수정",
            description = "ID를 이용해서 해당 카드 정보를 수정 합니다.",
            parameters = [
                Parameter(name = "id", description = "카드 ID"),
            ],
            responses = [
                ApiResponse(responseCode = "200", description = "카드 수정 성공"),
                ApiResponse(responseCode = "400", description = "request의 value가 형식이 잘못됨", content = [Content()]),
                ApiResponse(responseCode = "403", description = "권한 없음", content = [Content()]),
                ApiResponse(responseCode = "404", description = "정보를 찾을 수 없음", content = [Content()]),
            ],
    )
    @PatchMapping("/{id}")
    fun updateById(
            @PathVariable id: Long,
            @Valid
            @RequestBody
            cardRequest: CardRequest,
            @AuthenticationPrincipal
            principalDetails: PrincipalDetails,
    ): ResponseEntity<CardResponse> {
        val cardResponse = cardService.updateById(id, cardRequest, principalDetails)
        return ResponseEntity.ok(cardResponse)
    }

    @Operation(
            summary = "카드 정보 삭제",
            description = "ID를 이용해서 해당 카드 정보를 삭제 합니다.",
            parameters = [
                Parameter(name = "id", description = "카드 ID"),
            ],
            responses = [
                ApiResponse(responseCode = "200", description = "카드 삭제 성공"),
                ApiResponse(responseCode = "403", description = "권한 없음", content = [Content()]),
                ApiResponse(responseCode = "404", description = "정보를 찾을 수 없음", content = [Content()]),
            ],
    )
    @DeleteMapping("/{id}")
    fun deleteById(
            @PathVariable id: Long,
            @AuthenticationPrincipal
            principalDetails: PrincipalDetails,
    ): ResponseEntity<Unit> {
        cardService.deleteById(id, principalDetails)
        return ResponseEntity.ok(null)
    }
}