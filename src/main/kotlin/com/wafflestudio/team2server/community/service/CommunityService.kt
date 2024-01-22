package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.community.model.*

interface CommunityService {
	fun getCommunityList(cur: Long, seed: Int, distance: Int, count: Int, areaId: Int, authUserInfo: AuthUserInfo): CommunityListResponse
	fun findCommunityById(id: Long): Community
	fun create(communityRequest: CommunityRequest, authUserInfo: AuthUserInfo)
	fun update(communityRequest: CommunityUpdateRequest, userId: Long, id:Long)
	fun delete(userId: Long, id:Long)
	fun likeCommunity(userId: Long, id: Long)
	fun createComment(commentRequest: CommentRequest, userId: Long, id: Long)
	fun updateComment(commentUpdateRequest: CommentUpdateRequest, userId: Long, id: Long, commentId: Long)
	fun deleteComment(userId: Long, id: Long, commentId: Long)
	fun likeComment(userId: Long, id: Long, commentId: Long)
	fun getCommentList(userId: Long, id: Long): List<CommentListResponse>
}
