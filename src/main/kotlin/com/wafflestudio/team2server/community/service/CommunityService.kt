package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.community.controller.CommunityController
import com.wafflestudio.team2server.community.model.Community;

interface CommunityService {
	fun getCommunityList(cur: Long, seed: Int, areaId: Int, distance: Int): CommunityController.ListResponse
	fun findCommunityById(id: Long): Community
	fun create(communityRequest: CommunityController.CommunityRequest, userId: Long)
	fun update(communityRequest: CommunityController.CommunityUpdateRequest, userId: Long, id:Long)
	fun delete(userId: Long, id:Long)
	fun likeCommunity(userId: Long, id: Long)
	fun createComment(commentRequest: CommunityController.CommentRequest, userId: Long, id: Long)
	fun updateComment(commentUpdateRequest: CommunityController.CommentUpdateRequest, userId: Long, id: Long, commentId: Long)
	fun deleteComment(userId: Long, id: Long, commentId: Long)
}
