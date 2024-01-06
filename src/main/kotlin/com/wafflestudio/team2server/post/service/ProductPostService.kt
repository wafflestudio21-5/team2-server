package com.wafflestudio.team2server.post.service

import com.wafflestudio.team2server.post.controller.ProductPostController
import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.post.repository.ProductPostEntity
import com.wafflestudio.team2server.user.repository.UserEntity

interface ProductPostService {
	fun exists(id: Long): Boolean
	fun searchPostByTitleAndArea(title: String, refAreaId: List<Int>, distance: Int): List<ProductPost>
	fun create(postCreateRequest: ProductPostController.PostCreateRequest, userId: Long)
	fun update(postUpdateRequest: ProductPostController.PostUpdateRequest, userId: Long, id: Long, refresh: Boolean)
	fun findPostById(id: Long): ProductPost
	fun deleteById(id: Long)
	fun likePost(userId: Long, id: Long)
	fun unlikePost(userId: Long, id: Long)
	fun getLikedPosts(userId: Long): List<ProductPostEntity>
	fun getLikedUsers(postId: Long): List<UserEntity>

}
