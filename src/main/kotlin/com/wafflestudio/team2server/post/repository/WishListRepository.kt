package com.wafflestudio.team2server.post.repository

import org.springframework.data.jpa.repository.JpaRepository

interface WishListRepository : JpaRepository<WishListEntity, Long> {
	fun existsByUserIdAndPostId(userId: Long, postId: Long): Boolean
	fun findByUserIdAndPostId(userId: Long, postId: Long): WishListEntity
	fun findByUserId(userId: Long): List<WishListEntity>
	fun findByPostId(postId: Long): List<WishListEntity>
}
