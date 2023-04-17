package com.skhu.mypack.auth.dao

import com.skhu.mypack.auth.domain.Member
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
    fun findByName(name: String): Member?
    fun findAllByNameLike(query: String): Slice<Member>
    fun isExistsByName(name: String): Boolean
}