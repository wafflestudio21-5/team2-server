package com.wafflestudio.team2server.post.service

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.post.controller.ProductPostController
import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.user.model.User

interface ProductPostService {
	fun exists(id: Long): Boolean
	fun searchPostByKeyword(cur: Long, keyword: String, distance: Int, count: Int, areaId: Int, authUserInfo: AuthUserInfo): ProductPostController.ListResponse
	fun create(postCreateRequest: ProductPostController.PostCreateRequest, authUserInfo: AuthUserInfo)
	fun update(postUpdateRequest: ProductPostController.PostUpdateRequest, userId: Long, id: Long, refresh: Boolean)
	fun getPostById(id: Long, authUserInfo: AuthUserInfo): ProductPost
	fun deleteById(id: Long)
	fun likePost(userId: Long, postId: Long)
	fun unlikePost(userId: Long, postId: Long)
	fun getLikedPosts(userId: Long): List<ProductPostController.PostSummary>
	fun getLikedUsers(postId: Long): List<User>
	fun getPostListRandom(cur: Long, seed: Int, distance: Int, count: Int, areaId: Int, authUserInfo: AuthUserInfo): ProductPostController.ListResponse
}
