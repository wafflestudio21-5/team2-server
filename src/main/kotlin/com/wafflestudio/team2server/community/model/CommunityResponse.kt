package com.wafflestudio.team2server.community.model

data class CommunityResponse (
	val community: Community,
	val isLiked: Boolean,
	val nickname: String,
	val profileImg: String?,
	val areaInfo: String
)
