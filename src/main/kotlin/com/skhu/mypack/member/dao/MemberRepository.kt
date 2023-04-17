package com.skhu.mypack.member.dao

import com.skhu.mypack.member.domain.Member
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
    fun findByName(name: String): Member?
    fun findAllByNameLike(query: String, pageable: Pageable): Slice<Member>
    fun existsByName(name: String): Boolean
}