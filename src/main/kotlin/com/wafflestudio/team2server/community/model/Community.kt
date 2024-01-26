package com.wafflestudio.team2server.community.model

import com.wafflestudio.team2server.community.repository.CommunityEntity

data class Community(
	val id: Long? = null,
	val authorId: Long,
	val areaId: Long,
	val createdAt: Long,
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
		createdAt = communityEntity.createdAt.toEpochMilli(),
		title = communityEntity.title,
		description = communityEntity.description,
		viewCnt = communityEntity.viewCnt,
		likeCnt = communityEntity.likeCnt,
		chatCnt = communityEntity.chatCnt,
		repImg = communityEntity.repImg,
	)

}
