package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.community.controller.CommunityController
import com.wafflestudio.team2server.community.model.Community;

interface CommunityService {
	fun exist(id: Long): Boolean
	fun findCommunityById(id: Long): Community
	fun create(communityRequest: CommunityController.CommunityRequest, userId: Long)
	fun update(communityRequest: CommunityController.CommunityUpdateRequest, userId: Long, id:Long)
}
