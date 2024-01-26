package com.wafflestudio.team2server.community.model

data class CommentUpdateRequest(
	val comment: String?,
	val imgUrl: String? = null,
	val images: List<String>? = null
)
