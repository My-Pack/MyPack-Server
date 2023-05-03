package com.skhu.mypack.storage.dao

import com.querydsl.jpa.impl.JPAQueryFactory
import com.skhu.mypack.storage.domain.ImageFile
import com.skhu.mypack.storage.domain.QImageFile
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ImageFileRepositorySupport(
        private val jpaQueryFactory: JPAQueryFactory,
) {
    private val qImageFile = QImageFile.imageFile

    fun findAllByUseIsFalseAndBeforeOneDayAgo(): List<ImageFile> {
        val oneDayAgo = LocalDateTime.now().minusDays(1)

        return jpaQueryFactory.selectFrom(qImageFile)
            .where(
                qImageFile.isUse.eq(false),
                qImageFile.createdAt.before(oneDayAgo),
            )
            .fetch()
    }
}