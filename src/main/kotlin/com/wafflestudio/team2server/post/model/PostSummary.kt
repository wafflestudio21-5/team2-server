package com.wafflestudio.team2server.post.model

data class PostSummary(
	val id: Long,
	val title: String,
	val repImg: String,
	val createdAt: Long?,
	val refreshedAt: Long?,
	val chatCnt: Int,
	val wishCnt: Int,
	val sellPrice: Int,
	val sellingArea: String,
	val deadline: Long?,
	val type: String,
	val status: String,
)
