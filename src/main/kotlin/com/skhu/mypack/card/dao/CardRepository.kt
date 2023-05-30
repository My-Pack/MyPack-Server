package com.skhu.mypack.card.dao;

import com.skhu.mypack.card.domain.Card
import org.springframework.data.jpa.repository.JpaRepository

interface CardRepository : JpaRepository<Card, Long> {
}