package com.skhu.mypack.card.app

import com.skhu.mypack.card.dao.CardRepository
import com.skhu.mypack.card.domain.Card
import com.skhu.mypack.card.dto.request.CardRequest
import com.skhu.mypack.card.dto.response.CardResponse
import com.skhu.mypack.card.exception.CardNotFoundException
import com.skhu.mypack.card.exception.NoCardPermissionException
import com.skhu.mypack.global.auth.PrincipalDetails
import com.skhu.mypack.member.app.MemberService
import com.skhu.mypack.storage.app.ImageFileService
import org.springframework.stereotype.Service

@Service
class CardService(
        private val cardRepository: CardRepository,
        private val imageFileService: ImageFileService,
        private val memberService: MemberService,
) {
    fun create(cardRequest: CardRequest, principalDetails: PrincipalDetails): CardResponse {
        val member = memberService.findEntityByEmail(principalDetails.email)
        val image = imageFileService.findEntityById(cardRequest.imageId)
        val card = Card(
                title = cardRequest.title,
                content = cardRequest.content,
                image = image,
                color = cardRequest.color,
                theme = cardRequest.theme,
                member = member,
        )

        return CardResponse.of(cardRepository.save(card))
    }

    fun findById(id: Long): CardResponse {
        val card = findEntityById(id)
        return CardResponse.of(card)
    }

    fun updateById(id: Long, cardRequest: CardRequest, principalDetails: PrincipalDetails): CardResponse {
        val card = findEntityById(id)

        if (card.member.email != principalDetails.email) {
            throw NoCardPermissionException()
        }

        val image = imageFileService.findEntityById(cardRequest.imageId)
        card.update(cardRequest, image)

        return CardResponse.of(cardRepository.save(card))
    }

    fun deleteById(id: Long, principalDetails: PrincipalDetails) {
        val card = findEntityById(id)

        if (card.member.email != principalDetails.email) {
            throw NoCardPermissionException()
        }

        card.image.isUse = false
        cardRepository.deleteById(id)
    }

    fun findEntityById(id: Long): Card {
        return cardRepository.findById(id).orElseThrow { CardNotFoundException(id) }
    }
}