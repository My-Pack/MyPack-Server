package com.skhu.mypack.auth.app

import com.skhu.mypack.auth.dao.MemberRepository
import com.skhu.mypack.auth.domain.Member
import com.skhu.mypack.auth.domain.enum.Provider
import com.skhu.mypack.auth.domain.enum.Role
import com.skhu.mypack.auth.exception.MemberNotFoundException
import com.skhu.mypack.global.auth.PrincipalDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MemberService(
    private val memberRepository: MemberRepository,
) {

    @Transactional
    fun saveOrFindMember(email: String, role: Role, provider: Provider): PrincipalDetails {

        val member = try {
            findByEmail(email)
        } catch (e: MemberNotFoundException) {
            val newMember = Member(
                email = email,
                name = UUID.randomUUID().toString(),
                role = role,
                provider = provider
            )

            memberRepository.save(newMember)
        }

        return member.toPrincipal()
    }

    @Transactional(readOnly = true)
    fun findByEmail(email: String): Member {
        return memberRepository.findByEmail(email) ?: throw MemberNotFoundException(email)
    }
}