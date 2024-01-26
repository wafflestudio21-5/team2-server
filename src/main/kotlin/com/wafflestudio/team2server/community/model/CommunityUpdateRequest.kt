package com.wafflestudio.team2server.community.model

data class CommunityUpdateRequest(
	val title: String? = null,
	val description: String? = null,
	val repImg: String? = null,
	val images: List<String>? = null
)
