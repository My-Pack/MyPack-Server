package com.skhu.mypack.storage.exception

import com.skhu.mypack.global.error.CustomAbstractException
import org.springframework.http.HttpStatus

class CouldNotUploadS3Exception :
    CustomAbstractException(HttpStatus.BAD_REQUEST, "COULD_NOT_UPLOAD_S3", "S3에 파일을 업로드하는데 실패했습니다.")