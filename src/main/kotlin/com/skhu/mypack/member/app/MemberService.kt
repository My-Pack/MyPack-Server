package com.skhu.mypack.member.app

import com.skhu.mypack.global.auth.PrincipalDetails
import com.skhu.mypack.member.dao.MemberRepository
import com.skhu.mypack.member.domain.Member
import com.skhu.mypack.member.domain.enum.Provider
import com.skhu.mypack.member.domain.enum.Role
import com.skhu.mypack.member.dto.request.MemberUpdateRequest
import com.skhu.mypack.member.dto.response.MemberResponse
import com.skhu.mypack.member.exception.ExistsByNameException
import com.skhu.mypack.member.exception.MemberNotFoundException
import com.skhu.mypack.member.exception.NoMemberUpdatePermissionException
import com.skhu.mypack.storage.app.ImageFileService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val imageFileService: ImageFileService,
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
        return memberRepository.existsByName(name)
    }

    @Transactional(readOnly = true)
    fun findByName(name: String): MemberResponse {
        val member = memberRepository.findByName(name) ?: throw MemberNotFoundException(name)
        return MemberResponse.of(member)
    }

    @Transactional(readOnly = true)
    fun findAllByName(name: String, pageable: Pageable): Slice<MemberResponse> {
        val memberSlice = memberRepository.findAllByNameLike("%$name%", pageable)
        return memberSlice.map { MemberResponse.of(it) }
    }

    @Transactional(readOnly = true)
    fun findByEmail(email: String): MemberResponse {
        val member = memberRepository.findByEmail(email) ?: throw MemberNotFoundException(email)
        return MemberResponse.of(member)
    }

    @Transactional
    fun update(principalDetails: PrincipalDetails, memberUpdateRequest: MemberUpdateRequest): MemberResponse {
        val email = principalDetails.email
        val member = memberRepository.findByEmail(email) ?: throw MemberNotFoundException(email)
        if (member.name != memberUpdateRequest.oldName) {
            throw NoMemberUpdatePermissionException()
        }

        if (isExistsByName(memberUpdateRequest.newName)) {
            throw ExistsByNameException(memberUpdateRequest.newName)
        }

        val profileImage = memberUpdateRequest.profileImageId?.let {
            imageFileService.findEntityById(it)
        }
        val backgroundImage = memberUpdateRequest.backgroundImageId?.let {
            imageFileService.findEntityById(it)
        }

        member.update(memberUpdateRequest.newName, profileImage, backgroundImage)

        return MemberResponse.of(member)
    }
}