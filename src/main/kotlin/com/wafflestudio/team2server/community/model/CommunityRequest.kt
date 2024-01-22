package com.wafflestudio.team2server.community.model

data class CommunityRequest(
	val title: String = "",
	val description: String = "",
	val areaId: Int,
	val repImg: String = "",
	val images: List<String> = listOf(),
)
