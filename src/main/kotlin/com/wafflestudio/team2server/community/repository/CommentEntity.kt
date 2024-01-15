package com.wafflestudio.team2server.community.repository

import com.wafflestudio.team2server.user.repository.UserEntity
import jakarta.persistence.*
import java.time.Instant

@Entity(name = "community_comment")
class CommentEntity (
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0L,
	@OneToOne
	@JoinColumn(name = "user_id")
	val author: UserEntity,
	@ManyToOne
	@JoinColumn(name = "community_id")
	val community: CommunityEntity,
	var comment: String = "",
	val parentId: Long = -1,
	var imgUrl: String = "",
	var likeCnt: Int = 0,
	val createdAt: Instant = Instant.now(),
	var updatedAt: Instant
)
