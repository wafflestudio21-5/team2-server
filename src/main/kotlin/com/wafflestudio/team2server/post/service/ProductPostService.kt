package com.wafflestudio.team2server.post.service

import com.wafflestudio.team2server.post.controller.ProductPostController
import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.user.model.User

interface ProductPostService {
	fun exists(id: Long): Boolean
	fun searchPostByKeyword(cur: Long, keyword: String, distance: Int, count: Int, areaId: Int, userId: Long): ProductPostController.ListResponse
	fun create(postCreateRequest: ProductPostController.PostCreateRequest, userId: Long)
	fun update(postUpdateRequest: ProductPostController.PostUpdateRequest, userId: Long, id: Long, refresh: Boolean)
	fun getPostById(id: Long, userId: Long): ProductPost
	fun deleteById(id: Long)
	fun likePost(userId: Long, id: Long)
	fun unlikePost(userId: Long, id: Long)
	fun getLikedPosts(userId: Long): List<ProductPostController.PostSummary>
	fun getLikedUsers(postId: Long): List<User>
	fun getPostListRandom(cur: Long, seed: Int, distance: Int, count: Int, areaId: Int, userId: Long): ProductPostController.ListResponse
}
