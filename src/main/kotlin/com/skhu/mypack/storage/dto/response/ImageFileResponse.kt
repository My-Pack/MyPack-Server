package com.skhu.mypack.storage.dto.response

import com.skhu.mypack.storage.domain.ImageFile

data class ImageFileResponse(
    val id: Long,
    val originalName: String,
    val storedName: String,
    val s3Url: String,
    val isUse: Boolean,
) {

    companion object {
        fun of(imageFile: ImageFile): ImageFileResponse {
            return ImageFileResponse(
                id = imageFile.id!!,
                originalName = imageFile.originalName,
                storedName = imageFile.storedName,
                s3Url = imageFile.s3Url,
                isUse = imageFile.isUse,
            )
        }
    }
}