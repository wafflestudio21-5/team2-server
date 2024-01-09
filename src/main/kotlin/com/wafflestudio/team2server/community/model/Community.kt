package com.wafflestudio.team2server.community.model

import com.wafflestudio.team2server.community.repository.CommunityEntity
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
	constructor(communityEntity: CommunityEntity) : this(
		id = communityEntity.id,
		authorId = communityEntity.author.id,
		areaId = communityEntity.areaId,
		createdAt = communityEntity.createdAt,
		title = communityEntity.title,
		description = communityEntity.description,
		viewCnt = communityEntity.viewCnt,
		likeCnt = communityEntity.likeCnt,
		repImg = communityEntity.repImg,
		status = communityEntity.status.name

	)
	enum class CommunityStatus {
		CREATED,
		DELETED
	}
}
