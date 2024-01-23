package com.wafflestudio.team2server.review.model

data class TradeReviewRequest(
	val receiverId: Long,
	val description: String,
	val authorAreaId: Int,
)
