package com.wafflestudio.team2server.community.model

import com.wafflestudio.team2server.community.repository.CommunityEntity
import java.time.Instant

data class Community(
	val id: Long? = null,
	val authorId: Long,
	val areaId: Long,
	val createdAt: Instant,
	val title: String,
	val description: String,
	val viewCnt: Int,
	val likeCnt: Int,
	val chatCnt: Int,
	val repImg: String,
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
		chatCnt = communityEntity.chatCnt,
		repImg = communityEntity.repImg,
	)

}
