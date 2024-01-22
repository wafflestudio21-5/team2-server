package com.wafflestudio.team2server.post.service

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.post.model.*
import com.wafflestudio.team2server.user.model.User
import java.time.Instant

interface ProductPostService {
	fun exists(id: Long): Boolean
	fun searchPostByKeyword(cur: Long, keyword: String, distance: Int, count: Int, areaId: Int, authUserInfo: AuthUserInfo): ListResponse
	fun create(postCreateRequest: PostCreateRequest, authUserInfo: AuthUserInfo)
	fun update(postUpdateRequest: PostUpdateRequest, userId: Long, id: Long, refresh: Boolean)
	fun getPostById(id: Long, authUserInfo: AuthUserInfo): ProductPost
	fun deleteById(id: Long)
	fun likePost(userId: Long, postId: Long)
	fun unlikePost(userId: Long, postId: Long)
	fun getLikedPosts(userId: Long): List<PostSummary>
	fun getLikedUsers(postId: Long): List<User>
	fun getPostListRandom(cur: Long, seed: Int, distance: Int, count: Int, areaId: Int, authUserInfo: AuthUserInfo): ListResponse
	fun getAuctionPosts(userId: Long): List<PostSummary>
	fun bidList(postId: Long): List<BidInfo>
	fun bid(userId: Long, id: Long, bidPrice: Int, now: Instant)
}
