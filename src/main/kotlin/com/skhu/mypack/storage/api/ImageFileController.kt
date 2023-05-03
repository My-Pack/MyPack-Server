package com.skhu.mypack.storage.api

import com.skhu.mypack.storage.app.ImageFileService
import com.skhu.mypack.storage.dto.response.ImageFileResponse
import com.skhu.mypack.storage.exception.IsNotImageFileException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/images")
class ImageFileController(
    private val imageFileService: ImageFileService,
) {

    @PostMapping
    fun save(@RequestParam("file") file: MultipartFile): ResponseEntity<ImageFileResponse> {
        validateImageFile(file)
        return ResponseEntity.ok(imageFileService.save(file))
    }

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