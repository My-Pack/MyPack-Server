package com.skhu.mypack.storage.app

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.skhu.mypack.storage.exception.CouldNotUploadS3Exception
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Service
class S3StorageService(
    private val s3Client: AmazonS3Client,
    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,
) {

    fun uploadFile(multipartFile: MultipartFile, convertedFileName: String): String {
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentType = multipartFile.contentType
        objectMetadata.contentLength = multipartFile.size

        try {
            val inputStream = multipartFile.inputStream
            s3Client.putObject(
                PutObjectRequest(bucket, "mypack/$convertedFileName", inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
        } catch (e: IOException) {
            throw CouldNotUploadS3Exception()
        }

        return s3Client.getUrl(bucket, "mypack/$convertedFileName").toString()
    }

    fun removeFile(fileName: String) {
        s3Client.deleteObject(DeleteObjectRequest(bucket, "mypack/$fileName"))
    }
}