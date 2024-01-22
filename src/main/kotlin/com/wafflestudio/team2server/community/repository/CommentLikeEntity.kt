package com.wafflestudio.team2server.community.repository

import com.wafflestudio.team2server.common.util.BaseCreatedDateEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "comment_like")
class CommentLikeEntity (
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0L,
	val userId: Long,
	val commentId: Long
) : BaseCreatedDateEntity()
