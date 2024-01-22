package com.wafflestudio.team2server.community.model

import java.time.Instant

data class CommentListResponse (
	val id: Long,
	val nickname: String,
	val comment: String,
	val imgUrl: String,
	val createdAt: Instant?,
	val likeCnt: Int,
	// val areaId: Long,
	val childComments: List<CommentSummary> = emptyList()
)
