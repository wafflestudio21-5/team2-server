package com.wafflestudio.team2server.community.model

data class CommentRequest(
	val comment: String = "",
	val parentId: Long?,
	val imgUrl: String = "",
	val images: List<String> = listOf(),
)
