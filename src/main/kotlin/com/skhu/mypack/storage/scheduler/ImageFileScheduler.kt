package com.skhu.mypack.storage.scheduler

import com.skhu.mypack.storage.app.ImageFileService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ImageFileScheduler(
        private val imageFileService: ImageFileService
) {

    @Scheduled(cron = "0 0 0 * * *")
    fun deleteAllByNotUse() {
        imageFileService.deleteAllByUseIsFalse()
    }
}