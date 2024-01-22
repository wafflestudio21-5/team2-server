package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.community.controller.CommunityController
import com.wafflestudio.team2server.community.model.Community
import com.wafflestudio.team2server.community.repository.*
import com.wafflestudio.team2server.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Service
class CommunityServiceImpl(
	private val communityRepository: CommunityRepository,
	private val userRepository: UserRepository,
	private val communityLikeRepository: CommunityLikeRepository,
	private val commentRepository: CommentRepository,
	private val commentLikeRepository: CommentLikeRepository
) : CommunityService {
	override fun getCommunityList(cur: Long, seed: Int, areaId: Int, distance: Int): CommunityController.ListResponse {
		TODO("Not yet implemented")
	}
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
			chatCnt = 0,
			repImg = "",
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
		val user = userRepository.findById(userId).getOrNull() ?: throw BaniException(ErrorType.UNAUTHORIZED)
		val community = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		//if (community.author.id == userId) {
		//	TODO("본인이 누른 경우 어떻게 할 지 코드 추가 with client")
		//}
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

	@Transactional
	override fun createComment(commentRequest: CommunityController.CommentRequest, userId: Long, id: Long) {
		val user = userRepository.findById(userId).getOrNull() ?: throw BaniException(ErrorType.UNAUTHORIZED)
		val community = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		val comment = CommentEntity(
			author = user,
			community = community,
			comment = commentRequest.comment,
			parentId = commentRequest.parentId,
			imgUrl = "",
			likeCnt = 0,
			createdAt = Instant.now(),
			updatedAt = Instant.now(),
		)
		commentRepository.save(comment)
	}

	@Transactional
	override fun updateComment(commentUpdateRequest: CommunityController.CommentUpdateRequest, userId: Long, id: Long, commentId: Long) {
		val comment = commentRepository.findById(commentId).getOrNull() ?: throw BaniException(ErrorType.COMMENT_NOT_FOUND)
		if (comment.author.id != userId) {
			throw BaniException(ErrorType.UNAUTHORIZED)
		}
		if (comment.community.id != id) {
			throw BaniException(ErrorType.COMEMNT_NOT_MATCHED)
		}
		comment.comment = commentUpdateRequest.comment ?: comment.comment
		comment.updatedAt = Instant.now()
		commentRepository.save(comment)
	}

	@Transactional
	override fun deleteComment(userId: Long, id: Long, commentId: Long) {
		val comment = commentRepository.findById(commentId).getOrNull() ?: throw BaniException(ErrorType.COMMENT_NOT_FOUND)
		val community = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		commentRepository.delete(comment)
		community.chatCnt--
		communityRepository.save(community)
	}

	@Transactional
	override fun likeComment(userId: Long, id: Long, commentId: Long) {
		val comment = commentRepository.findById(commentId).getOrNull() ?: throw BaniException(ErrorType.COMMENT_NOT_FOUND)
		//if (community.author.id == userId) {
		//	TODO("본인이 누른 경우 어떻게 할 지 코드 추가 with client")
		//}
		// 커뮤니티 id를 저장해야할 지에 대한 고민
		//val community = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		if (!commentLikeRepository.existsByUserIdAndCommentId(userId, commentId)) {
			val commentLike = CommentLikeEntity(userId = userId, commentId = commentId)
			commentLikeRepository.save(commentLike)
			comment.likeCnt++
		} else {
			val commentLike = commentLikeRepository.findByUserIdAndCommentId(userId, commentId)
			commentLikeRepository.delete(commentLike)
			comment.likeCnt--
		}
		commentRepository.save(comment)
	}

}
