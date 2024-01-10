package com.wafflestudio.team2server.post.service

import com.wafflestudio.team2server.post.controller.ProductPostController
import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.user.model.User

interface ProductPostService {
	fun exists(id: Long): Boolean
	fun searchPostByKeyword(cur: Long, keyword: String, refAreaId: List<Int>, distance: Int): ProductPostController.ListResponse
	fun create(postCreateRequest: ProductPostController.PostCreateRequest, userId: Long)
	fun update(postUpdateRequest: ProductPostController.PostUpdateRequest, userId: Long, id: Long, refresh: Boolean)
	fun findPostById(id: Long): ProductPost
	fun deleteById(id: Long)
	fun likePost(userId: Long, id: Long)
	fun unlikePost(userId: Long, id: Long)
	fun getLikedPosts(userId: Long): List<ProductPost>
	fun getLikedUsers(postId: Long): List<User>
	fun getPostListRandom(cur: Long, seed: Int, areaId: Int, distance: Int): ProductPostController.ListResponse
}
