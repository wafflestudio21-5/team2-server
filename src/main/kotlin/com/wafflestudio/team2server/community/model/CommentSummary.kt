package com.wafflestudio.team2server.community.model

import java.time.Instant

data class CommentSummary (
	val id: Long,
	val nickname: String,
	val comment: String,
	val imgUrl: String,
	val createdAt: Instant?,
	val likeCnt: Int,
	// sval areaId: Long,
)
