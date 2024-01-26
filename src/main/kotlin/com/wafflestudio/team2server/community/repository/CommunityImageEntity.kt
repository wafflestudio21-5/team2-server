package com.wafflestudio.team2server.community.repos

import com.wafflestudio.team2server.community.repository.CommunityEntity
import jakarta.persistence.*
import java.time.Instant

@Entity(name = "community_img")
class CommunityImageEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	val url: String,
	@ManyToOne
	@JoinColumn(name = "community_id")
	val community: CommunityEntity,
	val createdAt: Instant = Instant.now(),
)
