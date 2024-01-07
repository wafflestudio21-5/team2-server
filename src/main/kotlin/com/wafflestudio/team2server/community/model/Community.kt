package com.wafflestudio.team2server.community.model

import java.time.LocalDateTime

data class Community (
	val id: Long? = null,
	val authorId: Long,
	val areaId: Long,
	val createdAt: LocalDateTime,
	val title: String,
	val description: String,
	val viewCnt: Int,
	val likeCnt: Int,
	val repImg: String,
	val status: String,
) {
	enum class CommunityStatus {
		CREATED,
		DELETED
	}
}
