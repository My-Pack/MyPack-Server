package com.skhu.mypack.global.auth.app

import com.skhu.mypack.auth.dao.MemberRepository
import com.skhu.mypack.auth.exception.MemberNotFoundException
import com.skhu.mypack.global.auth.PrincipalDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PrincipalService(private val memberRepository: MemberRepository) : UserDetailsService {
    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findByEmail(username) ?: throw MemberNotFoundException(username)
        return PrincipalDetails(member.email, member.role)
    }
}