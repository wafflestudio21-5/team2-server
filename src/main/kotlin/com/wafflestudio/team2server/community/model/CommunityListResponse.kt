package com.wafflestudio.team2server.community.model

data class CommunityListResponse(
	val data: List<CommunitySummary>,
	val cur: Long,
	val seed: Int?,
	val isLast: Boolean,
	val count: Int
)
