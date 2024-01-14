package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.community.controller.CommunityController
import com.wafflestudio.team2server.community.model.Community
import com.wafflestudio.team2server.community.repository.CommunityEntity
import com.wafflestudio.team2server.community.repository.CommunityLikeEntity
import com.wafflestudio.team2server.community.repository.CommunityLikeRepository
import com.wafflestudio.team2server.community.repository.CommunityRepository
import com.wafflestudio.team2server.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Service
class CommunityServiceImpl(
	private val communityRepository: CommunityRepository,
	private val userRepository: UserRepository,
	private val communityLikeRepository: CommunityLikeRepository
) : CommunityService {
	override fun findCommunityById(id: Long): Community {
		val community: CommunityEntity = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		return Community(community)
		// 추후 viewCnt 관련 로직 추가
	}

	@Transactional
	override fun create(communityRequest: CommunityController.CommunityRequest, userId: Long) {
		val user = userRepository.findById(userId).getOrNull() ?: throw BaniException(ErrorType.UNAUTHORIZED)
		// areaId, repImg 추후 수정
		val community = CommunityEntity(
			author = user,
			areaId = 0,
			createdAt = Instant.now(),
			title = communityRequest.title,
			description = communityRequest.description,
			viewCnt = 0,
			likeCnt = 0,
			repImg = "",
			status = Community.CommunityStatus.CREATED
		)
		communityRepository.save(community)
	}

	@Transactional
	override fun update(communityRequest: CommunityController.CommunityUpdateRequest, userId: Long, id: Long) {
		val community = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		if (community.author.id != userId) {
			throw BaniException(ErrorType.UNAUTHORIZED)
		}
		community.title = communityRequest.title ?: community.title
		community.description = communityRequest.description ?: community.description
		communityRepository.save(community)
	}

	@Transactional
	override fun delete(userId: Long, id: Long) {
		val community = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		if (community.author.id != userId) {
			throw BaniException(ErrorType.UNAUTHORIZED)
		} else (communityRepository.deleteById(id))
	}

	@Transactional
	override fun likeCommunity(userId: Long, id: Long) {
		val community = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		if (!communityLikeRepository.existsByUserIdAndCommunityId(userId, id)) {
			val communityLike = CommunityLikeEntity(userId = userId, communityId = id)
			communityLikeRepository.save(communityLike)
			community.likeCnt++
		} else {
			val communityLike = communityLikeRepository.findByUserIdAndCommunityId(userId, id)
			communityLikeRepository.delete(communityLike)
			community.likeCnt--
		}
		communityRepository.save(community)
	}
}
