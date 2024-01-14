package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.community.controller.CommunityController
import com.wafflestudio.team2server.community.model.Community;

interface CommunityService {
	fun findCommunityById(id: Long): Community
	fun create(communityRequest: CommunityController.CommunityRequest, userId: Long)
	fun update(communityRequest: CommunityController.CommunityUpdateRequest, userId: Long, id:Long)
	fun delete(userId: Long, id:Long)
	fun likeCommunity(userId: Long, id: Long)
}
