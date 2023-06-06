package com.skhu.mypack.card.dao

import com.querydsl.jpa.impl.JPAQueryFactory
import com.skhu.mypack.card.domain.Card
import com.skhu.mypack.card.domain.QCard
import com.skhu.mypack.member.domain.QMember
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class CardRepositorySupport(
        private val jpaQueryFactory: JPAQueryFactory,
) {
    private val qCard: QCard = QCard.card

    @Transactional(readOnly = true)
    fun findAll(
            title: String?,
            content: String?,
            color: String?,
            theme: String?,
            memberName: String?,
            pageable: Pageable,
    ): Slice<Card> {
        val cards: MutableList<Card> = jpaQueryFactory.selectFrom(qCard)
                .leftJoin(qCard.member, QMember.member)
                .where(
                        title?.let { qCard.title.contains(title) },
                        content?.let { qCard.content.contains(content) },
                        color?.let { qCard.color.eq(color) },
                        theme?.let { qCard.theme.eq(theme) },
                        memberName?.let { qCard.member.name.eq(memberName) },
                )
                .orderBy(qCard.id.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize + 1L)
                .fetch()
                .toMutableList()

        var hasNext = false
        if (cards.size > pageable.pageSize) {
            cards.removeAt(pageable.pageSize)
            hasNext = true
        }

        return SliceImpl(cards, pageable, hasNext)
    }
}