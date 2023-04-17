package com.skhu.mypack.auth.app

import com.skhu.mypack.auth.dao.MemberRepository
import com.skhu.mypack.auth.domain.Member
import com.skhu.mypack.auth.domain.enum.Provider
import com.skhu.mypack.auth.domain.enum.Role
import com.skhu.mypack.auth.dto.request.MemberUpdateRequest
import com.skhu.mypack.auth.dto.response.MemberResponse
import com.skhu.mypack.auth.exception.MemberNotFoundException
import com.skhu.mypack.auth.exception.NoMemberUpdatePermissionException
import com.skhu.mypack.global.auth.PrincipalDetails
import org.springframework.data.domain.Slice
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
            memberRepository.findByEmail(email) ?: throw MemberNotFoundException(email)
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
    fun isExistsByName(name: String): Boolean {
        return memberRepository.isExistsByName(name)
    }

    @Transactional(readOnly = true)
    fun findByName(name: String): MemberResponse {
        val member = memberRepository.findByName(name) ?: throw MemberNotFoundException(name)
        return MemberResponse.of(member)
    }

    @Transactional(readOnly = true)
    fun findAllByName(name: String): Slice<MemberResponse> {
        val memberSlice = memberRepository.findAllByNameLike("%$name%")
        return memberSlice.map { MemberResponse.of(it) }
    }

    @Transactional(readOnly = true)
    fun findByEmail(email: String): MemberResponse {
        val member = memberRepository.findByEmail(email) ?: throw MemberNotFoundException(email)
        return MemberResponse.of(member)
    }

    @Transactional
    fun updateName(principalDetails: PrincipalDetails, memberUpdateRequest: MemberUpdateRequest): MemberResponse {
        val email = principalDetails.email
        val member = memberRepository.findByEmail(email) ?: throw MemberNotFoundException(email)
        if (member.name != memberUpdateRequest.oldName) {
            throw NoMemberUpdatePermissionException()
        }

        member.update(memberUpdateRequest)

        return MemberResponse.of(member)
    }
}