package com.wafflestudio.team2server.post.repository

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "wish_list")
class WishListEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0L,
	val userId: Long,
	val postId: Long,
)
