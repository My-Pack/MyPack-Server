package com.skhu.mypack.member.domain

import com.skhu.mypack.global.auth.PrincipalDetails
import com.skhu.mypack.member.domain.enum.Provider
import com.skhu.mypack.member.domain.enum.Role
import com.skhu.mypack.storage.domain.ImageFile
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    @Column(name = "email", unique = true, nullable = false)
    var email: String,
    @Column(name = "name", unique = true, nullable = false)
    var name: String = "",
    @Enumerated
    @Column(name = "provider", nullable = false)
    var provider: Provider = Provider.NONE,
    @Enumerated
    @Column(name = "role", nullable = false)
    var role: Role = Role.ROLE_USER,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    var profileImage: ImageFile? = null,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "background_image_id")
    var backgroundImage: ImageFile? = null,
) {
    fun toPrincipal(): PrincipalDetails {
        return PrincipalDetails(
            email,
            role
        )
    }

    fun update(name: String, profileImage: ImageFile?, backgroundImage: ImageFile?) {
        this.name = name
        this.profileImage?.updateUse(false)
        this.backgroundImage?.updateUse(false)
        profileImage?.updateUse(true)
        backgroundImage?.updateUse(true)
        this.profileImage = profileImage
        this.backgroundImage = backgroundImage
    }
}