package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.community.model.Community
import com.wafflestudio.team2server.community.repository.CommunityEntity
import com.wafflestudio.team2server.community.repository.CommunityRepository
import org.springframework.stereotype.Service;
import kotlin.jvm.optionals.getOrNull

@Service
class CommunityServiceImpl(
	private val communityRepository: CommunityRepository
) : CommunityService {

	override fun exist(id: Long): Boolean {
		return communityRepository.findById(id).getOrNull() != null;
	}

	override fun findCommunityById(id: Long): Community {
		TODO()
		// 에러코드 작성
		//	val communityEntity : CommunityEntity = communityRepository.findById(id)
		//	return Community(communityEntity)
	}

	}
