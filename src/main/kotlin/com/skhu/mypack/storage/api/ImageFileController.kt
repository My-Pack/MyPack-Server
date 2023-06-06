package com.skhu.mypack.storage.api

import com.skhu.mypack.storage.app.ImageFileService
import com.skhu.mypack.storage.dto.response.ImageFileResponse
import com.skhu.mypack.storage.exception.IsNotImageFileException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/images")
class ImageFileController(
    private val imageFileService: ImageFileService,
) {

    @Operation(
            summary = "이미지 업로드",
            description = "이미지를 업로드합니다. content type이 image로 시작해야 업로드가 가능합니다.",
            responses = [
                ApiResponse(responseCode = "201", description = "이미지 업로드 성공"),
                ApiResponse(responseCode = "400", description = "이미지 파일이 아님", content = [Content()]),
            ],
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun save(
            @Parameter(description = "이미지 파일")
            @RequestParam("file")
            file: MultipartFile
    ): ResponseEntity<ImageFileResponse> {
        validateImageFile(file)

        val responseBody = imageFileService.save(file);
        val location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseBody.id)
                .toUri()

        return ResponseEntity.created(location).body(responseBody)
    }

    @Operation(
            summary = "이미지 정보 조회",
            description = "ID를 이용해서 해당 이미지 정보를 조회 합니다.",
            parameters = [
                Parameter(name = "id", description = "이미지 파일 ID"),
            ],
            responses = [
                ApiResponse(responseCode = "200", description = "이미지 조회 성공"),
                ApiResponse(responseCode = "404", description = "정보를 찾을 수 없음", content = [Content()]),
            ],
    )
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<ImageFileResponse> {
        return ResponseEntity.ok(imageFileService.findById(id))
    }

    private fun validateImageFile(file: MultipartFile) {
        if (!file.contentType!!.startsWith("image")) {
            throw IsNotImageFileException()
        }
    }
}