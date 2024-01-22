package com.wafflestudio.team2server.post.model

data class ListResponse(
	val data: List<PostSummary>,
	val cur: Long,
	val seed: Int?,
	val isLast: Boolean,
	val count: Int,
)
