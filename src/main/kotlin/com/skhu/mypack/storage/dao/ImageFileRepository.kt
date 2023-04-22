package com.skhu.mypack.storage.dao

import com.skhu.mypack.storage.domain.ImageFile
import org.springframework.data.jpa.repository.JpaRepository

interface ImageFileRepository : JpaRepository<ImageFile, Long> {
}