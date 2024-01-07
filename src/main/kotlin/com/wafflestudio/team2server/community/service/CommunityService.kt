package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.community.model.Community;

interface CommunityService {
	fun exist(id: Long): Boolean
	fun findCommunityById(id: Long): Community

}
