package com.wafflestudio.team2server.community.repository

import com.wafflestudio.team2server.common.util.BaseCreatedDateEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "community_like")
class CommunityLikeEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0L,
	val userId: Long,
	val communityId: Long
) : BaseCreatedDateEntity()
