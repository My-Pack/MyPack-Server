package com.skhu.mypack.member.domain

import com.skhu.mypack.global.auth.PrincipalDetails
import com.skhu.mypack.member.domain.enum.Provider
import com.skhu.mypack.member.domain.enum.Role
import com.skhu.mypack.member.dto.request.MemberUpdateRequest
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

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
) {
    fun toPrincipal(): PrincipalDetails {
        return PrincipalDetails(
            email,
            role
        )
    }

    fun update(memberUpdateRequest: MemberUpdateRequest) {
        this.name = memberUpdateRequest.newName
    }
}