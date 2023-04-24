package com.skhu.mypack.storage.domain

import com.skhu.mypack.global.common.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class ImageFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    @Column(name = "original_name", nullable = false)
    var originalName: String,
    @Column(name = "stored_name", nullable = false)
    var storedName: String,
    @Column(name = "s3_url", nullable = false)
    var s3Url: String,
    @Column(name = "is_use", nullable = false)
    var isUse: Boolean = false,
) : BaseTimeEntity() {

    fun updateUse(isUse: Boolean) {
        this.isUse = isUse
    }
}