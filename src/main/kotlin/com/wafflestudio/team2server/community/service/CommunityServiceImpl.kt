package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.community.model.Community
import com.wafflestudio.team2server.community.repository.CommunityEntity
import com.wafflestudio.team2server.community.repository.CommunityRepository
import org.springframework.stereotype.Service;
import kotlin.jvm.optionals.getOrNull

@Service
class CommunityServiceImpl(
	private val communityRepository: CommunityRepository
): CommunityService {

	override fun exist(id: Long): Boolean {
		return communityRepository.findById(id).getOrNull() != null;
	}

	override fun findCommunityById(id: Long): Community {
		val communityEntity : CommunityEntity = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		return Community(communityEntity)
	}
	fun Community(it: CommunityEntity): Community {
		return Community(
			id = it.id,
			authorId = it.author.id,
			areaId = it.areaId,
			createdAt = it.createdAt,
			title = it.title,
			description = it.description,
			viewCnt = it.viewCnt,
			likeCnt = it.likeCnt,
			repImg = it.repImg,
			status = it.status.name
		)
	}
	}
