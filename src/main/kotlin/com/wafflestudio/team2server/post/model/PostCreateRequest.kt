package com.wafflestudio.team2server.post.model

data class PostCreateRequest(
	val areaId: Int,
	val title: String = "",
	val description: String = "",
	val type: String = "NEW",
	val repImg: String = "",
	val images: List<String> = listOf(),
	val offerYn: Boolean = false,
	val deadline: Long? = null,
	val sellPrice: Int
)
