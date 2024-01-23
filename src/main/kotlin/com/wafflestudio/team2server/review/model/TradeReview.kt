package com.wafflestudio.team2server.review.model

data class TradeReview(
	val id: Long,
	val senderId: Long,
	val senderName: String,
	val senderAreaName: String,
	val senderProfileImg: String,
	val createdAt: Long,
	val description: String,
)
