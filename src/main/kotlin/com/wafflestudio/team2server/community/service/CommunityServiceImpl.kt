package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.community.controller.CommunityController
import com.wafflestudio.team2server.community.model.Community
import com.wafflestudio.team2server.community.repository.CommunityEntity
import com.wafflestudio.team2server.community.repository.CommunityRepository
import com.wafflestudio.team2server.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service;
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Service
class CommunityServiceImpl(
	private val communityRepository: CommunityRepository,
	private val userRepository: UserRepository,
): CommunityService {

	override fun exist(id: Long): Boolean {
		return communityRepository.findById(id).getOrNull() != null;
	}

	override fun findCommunityById(id: Long): Community {
		val communityEntity: CommunityEntity = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		return Community(communityEntity)
	}

	@Transactional
	override fun create(communityRequest: CommunityController.CommunityRequest, userId: Long) {
		val user = userRepository.findById(userId).getOrNull() ?: throw BaniException(ErrorType.UNAUTHORIZED)
		// areaId, repImg 추후 수정
		val communityEntity = CommunityEntity(
			author = user,
			areaId = 0,
			createdAt = LocalDateTime.now(),
			title = communityRequest.title,
			description = communityRequest.description,
			viewCnt = 0,
			likeCnt = 0,
			repImg = "",
			status = Community.CommunityStatus.CREATED
		)
		communityRepository.save(communityEntity)
	}

	@Transactional
	override fun update(communityRequest: CommunityController.CommunityUpdateRequest, userId: Long, id: Long) {
		val communityEntity = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		if (communityEntity.author.id != userId) { throw BaniException(ErrorType.UNAUTHORIZED) }
		communityEntity.title = communityRequest.title ?: communityEntity.title
		communityEntity.description = communityRequest.description ?: communityEntity.description
		communityRepository.save(communityEntity)
	}

//	fun Community(it: CommunityEntity): Community {
//		return Community(
//			id = it.id,
//			authorId = it.author.id,
//			areaId = it.areaId,
//			createdAt = it.createdAt,
//			title = it.title,
//			description = it.description,
//			viewCnt = it.viewCnt,
//			likeCnt = it.likeCnt,
//			repImg = it.repImg,
//			status = it.status.name
//		)
//	}
	}
