package com.wafflestudio.team2server.review.repository

import com.wafflestudio.team2server.post.repository.ProductPostEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TradeReviewRepository : JpaRepository<TradeReviewEntity, Long> {
	fun findAllByReceiverIdAndAuthorType(receiverId: Long, authorType: TradeReviewEntity.AuthorType): List<TradeReviewEntity>
	fun existsByPostAndAuthorType(post: ProductPostEntity, authorType: TradeReviewEntity.AuthorType): Boolean
}
