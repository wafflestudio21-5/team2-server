package com.wafflestudio.team2server.community.repos

import com.wafflestudio.team2server.community.repository.CommentEntity
import com.wafflestudio.team2server.community.repository.CommunityEntity
import jakarta.persistence.*
import java.time.Instant

@Entity(name = "comment_img")
class CommentImageEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	val url: String,
	@ManyToOne
	@JoinColumn(name = "comment_id")
	val comment: CommentEntity,
	val createdAt: Instant = Instant.now(),
)
