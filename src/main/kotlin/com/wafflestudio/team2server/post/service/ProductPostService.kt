package com.wafflestudio.team2server.post.service

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.post.model.*
import com.wafflestudio.team2server.user.model.User

interface ProductPostService {
	fun exists(id: Long): Boolean
	fun searchPostByKeyword(cur: Long, keyword: String, distance: Int, count: Int, areaId: Int, authUserInfo: AuthUserInfo): ListResponse
	fun create(postCreateRequest: PostCreateRequest, authUserInfo: AuthUserInfo)
	fun update(postUpdateRequest: PostUpdateRequest, userId: Long, id: Long, refresh: Boolean)
	fun getPostById(id: Long, authUserInfo: AuthUserInfo): ProductPost
	fun deleteById(id: Long)
	fun likePost(userId: Long, id: Long)
	fun unlikePost(userId: Long, id: Long)
	fun getLikedPosts(userId: Long): List<PostSummary>
	fun getLikedUsers(postId: Long): List<User>
	fun getPostListRandom(cur: Long, seed: Int, distance: Int, count: Int, areaId: Int, authUserInfo: AuthUserInfo): ListResponse
	fun bidList(postId: Long): List<BidSummary>
	fun placeBid(userId: Long, id: Long, bidPrice: Int)
}
