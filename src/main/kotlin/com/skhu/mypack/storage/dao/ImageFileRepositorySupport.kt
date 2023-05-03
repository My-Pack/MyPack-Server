package com.skhu.mypack.storage.dao

import com.querydsl.jpa.impl.JPAQueryFactory
import com.skhu.mypack.storage.domain.ImageFile
import com.skhu.mypack.storage.domain.QImageFile
import org.springframework.stereotype.Repository

@Repository
class ImageFileRepositorySupport(
        private val jpaQueryFactory: JPAQueryFactory,
) {
    private val qImageFile = QImageFile.imageFile

    fun findAllUseIsFalse(): List<ImageFile> {
        return jpaQueryFactory.selectFrom(qImageFile)
                .where(qImageFile.isUse.eq(false))
                .fetch()
    }
}