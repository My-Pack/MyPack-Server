package com.skhu.mypack.storage.app

import com.skhu.mypack.storage.dao.ImageFileRepository
import com.skhu.mypack.storage.domain.ImageFile
import com.skhu.mypack.storage.dto.response.ImageFileResponse
import com.skhu.mypack.storage.exception.NotImageFileFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class ImageFileService(
    private val imageFileRepository: ImageFileRepository,
    private val s3StorageService: S3StorageService,
) {

    @Transactional
    fun save(multipartFile: MultipartFile): ImageFileResponse {
        val randomName = createRandomName()
        val s3Url = s3StorageService.uploadFile(multipartFile, randomName)
        val imageFile = imageFileRepository.save(
            ImageFile(
                originalName = multipartFile.originalFilename!!,
                storedName = randomName,
                s3Url = s3Url,
            )
        )

        return ImageFileResponse.of(imageFile)
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): ImageFileResponse {
        val imageFile = findEntityById(id)
        return ImageFileResponse.of(imageFile)
    }

    @Transactional
    fun deleteById(id: Long) {
        val imageFile = findEntityById(id)
        s3StorageService.removeFile(imageFile.storedName)
        imageFileRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findEntityById(id: Long): ImageFile {
        return imageFileRepository.findByIdOrNull(id) ?: throw NotImageFileFoundException(id)
    }

    private fun createRandomName(): String {
        return UUID.randomUUID().toString()
    }
}