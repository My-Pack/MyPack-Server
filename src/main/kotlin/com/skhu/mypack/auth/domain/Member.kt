package com.skhu.mypack.auth.domain

import com.skhu.mypack.auth.domain.enum.Provider
import com.skhu.mypack.auth.domain.enum.Role
import com.skhu.mypack.global.auth.PrincipalDetails
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
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
    @Column(name = "is_signup", nullable = false)
    var isSignUp: Boolean = false,
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
            role,
            isSignUp
        )
    }
}