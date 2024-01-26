package com.wafflestudio.team2server.community.model

import java.time.Instant

data class CommunitySummary(
	val id: Long,
	val title: String,
	val repImg: String,
	val createdAt: Instant?,
	val viewCnt: Int,
	val likeCnt: Int,
	val chatCnt: Int,
	val description: String,
	val areaId: String
)
