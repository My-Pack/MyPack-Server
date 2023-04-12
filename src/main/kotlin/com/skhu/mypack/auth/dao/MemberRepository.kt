package com.skhu.mypack.auth.dao

import com.skhu.mypack.auth.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
}