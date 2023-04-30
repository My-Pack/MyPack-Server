package com.skhu.mypack.app

import com.skhu.mypack.member.app.MemberService
import com.skhu.mypack.member.dao.MemberRepository
import com.skhu.mypack.member.domain.Member
import com.skhu.mypack.member.domain.enum.Provider
import com.skhu.mypack.member.domain.enum.Role
import com.skhu.mypack.member.exception.MemberNotFoundException
import com.skhu.mypack.storage.app.ImageFileService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberServiceTests {

    @MockK
    private lateinit var memberRepository: MemberRepository

    @MockK
    private lateinit var imageFileService: ImageFileService

    @InjectMockKs
    private lateinit var memberService: MemberService

    @Nested
    inner class SaveOrFindMember {
        @Nested
        inner class Success {
            private val email = "email"
            private val role = Role.ROLE_USER
            private val provider = Provider.GOOGLE

            @Test
            fun `회원을 저장하는 경우`() {
                // given
                every { memberRepository.findByEmail(email) } throws MemberNotFoundException(email)
                every {
                    memberRepository.save(any())
                } returns Member(email = email, name = "random_name", role = role, provider = provider)

                // when
                val savedMember = memberService.saveOrFindMember(email, role, provider)

                // then
                verifyOrder {
                    memberRepository.findByEmail(email)
                    memberRepository.save(any())
                }

                assertSame(email, savedMember.email)
            }

            @Test
            fun `회원을 찾는 경우`() {
                // given
                every {
                    memberRepository.findByEmail(email)
                } returns Member(email = email, name = "random_name", role = role, provider = provider)

                // when
                val foundMember = memberService.saveOrFindMember(email, role, provider)

                // then
                verify {
                    memberRepository.findByEmail(email)
                }

                assertSame(email, foundMember.email)
            }
        }
    }

    @Nested
    inner class IsExistsByName {
        @Nested
        inner class Success {
            private val name = "name"

            @Test
            fun `회원이 존재하는 경우`() {
                // given
                every { memberRepository.existsByName(name) } returns true

                // when
                val isExists = memberService.isExistsByName(name)

                // then
                verify {
                    memberRepository.existsByName(name)
                }

                assertTrue(isExists)
            }

            @Test
            fun `회원이 존재하지 않는 경우`() {
                // given
                every { memberRepository.existsByName(name) } returns false

                // when
                val isExists = memberService.isExistsByName(name)

                // then
                verify {
                    memberRepository.existsByName(name)
                }

                assertFalse(isExists)
            }
        }
    }

    @Nested
    inner class FindByName {
        private val name = "name"

        @Nested
        inner class Success {
            @Test
            fun `회원을 찾았을 경우`() {
                // given
                every {
                    memberRepository.findByName(name)
                } returns Member(
                    id = 1,
                    email = "email",
                    name = name,
                    role = Role.ROLE_USER,
                    provider = Provider.GOOGLE
                )

                // when
                val foundMember = memberService.findByName(name)

                // then
                verify {
                    memberRepository.findByName(name)
                }

                assertSame(name, foundMember.name)
            }
        }

        @Nested
        inner class Fail {
            @Test
            fun `회원을 찾지 못했을 경우`() {
                // given
                every { memberRepository.findByName(name) } throws MemberNotFoundException(name)

                // when then
                assertThrows<MemberNotFoundException> {
                    memberService.findByName(name)
                }
                verify {
                    memberRepository.findByName(name)
                }
            }
        }
    }

    @Nested
    inner class FindAllByName {
        @Nested
        inner class Success {
            private val name = "name"
            private val pageable = PageRequest.of(0, 10)

            @Test
            fun `회원들을 찾았을 경우`() {
                // given
                val members = listOf(
                    Member(id = 1, email = "email1", name = "name1", role = Role.ROLE_USER, provider = Provider.GOOGLE),
                    Member(id = 2, email = "email2", name = "name1", role = Role.ROLE_USER, provider = Provider.GOOGLE),
                    Member(id = 3, email = "email3", name = "name3", role = Role.ROLE_USER, provider = Provider.GOOGLE),
                )
                every {
                    memberRepository.findAllByNameLike("%$name%", any())
                } returns PageImpl(members, pageable, members.size.toLong())

                // when
                val foundMembers = memberService.findAllByName(name, pageable)

                // then
                verify {
                    memberRepository.findAllByNameLike("%$name%", pageable)
                }

                assertSame(foundMembers.content.size, members.size)
            }

            @Test
            fun `회원들이 없을 경우`() {
                // given
                val members = emptyList<Member>()
                every {
                    memberRepository.findAllByNameLike("%$name%", any())
                } returns PageImpl(members, pageable, 0)

                // when
                val foundMembers = memberService.findAllByName(name, pageable)

                // then
                verify {
                    memberRepository.findAllByNameLike("%$name%", pageable)
                }

                assertSame(foundMembers.content.size, 0)
            }
        }
    }

    @Nested
    inner class FindByEmail {
        private val email = "email"

        @Nested
        inner class Success {
            @Test
            fun `회원을 찾았을 경우`() {
                // given
                every {
                    memberRepository.findByEmail(email)
                } returns Member(
                    id = 1,
                    email = email,
                    name = "name",
                    role = Role.ROLE_USER,
                    provider = Provider.GOOGLE
                )

                // when
                val foundMember = memberService.findByEmail(email)

                // then
                verify {
                    memberRepository.findByEmail(email)
                }

                assertSame(email, foundMember.email)
            }
        }

        @Nested
        inner class Fail {
            @Test
            fun `회원을 찾지 못했을 경우`() {
                // given
                every { memberRepository.findByEmail(email) } throws MemberNotFoundException(email)

                // when then
                assertThrows<MemberNotFoundException> {
                    memberService.findByEmail(email)
                }
                verify {
                    memberRepository.findByEmail(email)
                }
            }
        }
    }

//    @Nested
//    inner class Update {
//        private val principalDetails = PrincipalDetails("email", Role.ROLE_USER)
//        private val memberUpdateRequest = MemberUpdateRequest("old_name", "new_name", 1, 2)
//
//        @Nested
//        inner class Success {
//
//            @Test
//            fun `성공적으로 업데이트`() {
//                // given
//                every {
//                    memberRepository.findByEmail(principalDetails.email)
//                } returns Member(id = 1, email = principalDetails.email, name = "old_name")
//
//                // when
//                val updateMember = memberService.update(principalDetails, memberUpdateRequest)
//
//                // then
//                verify {
//                    memberRepository.findByEmail(principalDetails.email)
//                }
//
//                assertSame(updateMember.name, memberUpdateRequest.newName)
//            }
//        }
//
//        @Nested
//        inner class Fail {
//
//            @Test
//            fun `멤버가 없을 경우`() {
//                // given
//                every {
//                    memberRepository.findByEmail(principalDetails.email)
//                } throws MemberNotFoundException(principalDetails.email)
//
//                // when then
//                assertThrows<MemberNotFoundException> {
//                    memberService.update(principalDetails, memberUpdateRequest)
//                }
//                verify { memberRepository.findByEmail(principalDetails.email) }
//            }
//
//            @Test
//            fun `요청자와 대상자가 다를 경우`() {
//                // given
//                every {
//                    memberRepository.findByEmail(principalDetails.email)
//                } returns Member(id = 1, email = principalDetails.email, name = "other_name")
//
//                // when then
//                assertThrows<NoMemberUpdatePermissionException> {
//                    memberService.update(principalDetails, memberUpdateRequest)
//                }
//                verify { memberRepository.findByEmail(principalDetails.email) }
//            }
//        }
//    }
}