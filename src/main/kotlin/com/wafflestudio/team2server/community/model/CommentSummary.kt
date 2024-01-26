package com.wafflestudio.team2server.community.model

import java.time.Instant

data class CommentSummary (
	val id: Long,
	val nickname: String,
	val comment: String,
	val imgUrl: String,
	val createdAt: Long?,
	val likeCnt: Int,
	// sval areaId: Long,
	val isLiked: Boolean,
	val images: List<String>,
)
