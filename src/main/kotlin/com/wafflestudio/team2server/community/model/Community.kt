package com.wafflestudio.team2server.community.model

import com.wafflestudio.team2server.community.repository.CommunityEntity
import java.time.Instant

data class Community(
	val id: Long? = null,
	val authorId: Long,
	val areaId: Int,
	val createdAt: Long,
	val title: String,
	val description: String,
	val viewCnt: Int,
	val likeCnt: Int,
	val chatCnt: Int,
	val repImg: String,
	val images: List<String>,
) {
	constructor(communityEntity: CommunityEntity) : this(
		id = communityEntity.id,
		authorId = communityEntity.author.id,
		areaId = communityEntity.areaInfo.id,
		createdAt = communityEntity.createdAt.toEpochMilli(),
		title = communityEntity.title,
		description = communityEntity.description,
		viewCnt = communityEntity.viewCnt,
		likeCnt = communityEntity.likeCnt,
		chatCnt = communityEntity.chatCnt,
		repImg = communityEntity.repImg,
		images = communityEntity.images.map { it.url },
	)

}
