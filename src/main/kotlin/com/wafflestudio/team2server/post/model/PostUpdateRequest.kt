package com.wafflestudio.team2server.post.model

data class PostUpdateRequest(
	val title: String? = null,
	val description: String? = null,
	val type: String? = null,
	val repImg: String? = null,
	val images: List<String>? = null,
	val status: String? = null,
	val offerYn: Boolean? = null,
	val deadline: Long? = null,
	val hiddenYn: Boolean? = null,
	val sellPrice: Int? = null,
)
