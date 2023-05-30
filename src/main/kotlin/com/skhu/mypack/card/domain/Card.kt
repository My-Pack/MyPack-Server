package com.skhu.mypack.card.domain

import com.skhu.mypack.card.dto.request.CardRequest
import com.skhu.mypack.global.common.BaseTimeEntity
import com.skhu.mypack.member.domain.Member
import com.skhu.mypack.storage.domain.ImageFile
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne

@Entity
class Card(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    @Column(name = "title", nullable = false)
    var title: String,
    @Column(name = "content", nullable = false)
    var content: String,
    @Column(name = "like_count", nullable = false)
    var likeCount: Int = 0,
    @Column(name = "comment_count", nullable = false)
    var commentCount: Int = 0,
    @Column(name = "color", nullable = false)
    var color: String,
    @Column(name = "theme", nullable = false)
    var theme: String,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    var image: ImageFile,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member,
) : BaseTimeEntity() {
    fun update(request: CardRequest, image: ImageFile?) {
        this.title = request.title
        this.content = request.title
        this.color = request.color
        this.theme = request.theme

        image?.let {
            this.image.updateUse(false)
            image.updateUse(true)
            this.image = image
        }
    }
}