package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.area.service.AreaService
import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.community.model.*
import com.wafflestudio.team2server.community.repos.CommunityImageEntity
import com.wafflestudio.team2server.community.repository.*
import com.wafflestudio.team2server.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import kotlin.jvm.optionals.getOrNull
import kotlin.math.min

@Service
class CommunityServiceImpl(
	private val communityRepository: CommunityRepository,
	private val userRepository: UserRepository,
	private val communityLikeRepository: CommunityLikeRepository,
	private val commentRepository: CommentRepository,
	private val commentLikeRepository: CommentLikeRepository,
	private val areaService: AreaService,
	private val communityImageRepository: CommunityImageRepository
) : CommunityService {
	override fun getCommunityList(cur: Long, seed: Int, distance: Int, count: Int, areaId: Int, authUserInfo: AuthUserInfo): CommunityListResponse {
		check(areaId in authUserInfo.refAreaIds) {
			throw BaniException(ErrorType.INVALID_PARAMETER)
		}
		val cur = if (count % 300 == 0) Long.MAX_VALUE else cur
		val fetch = communityRepository.findRandom(cur, seed, areaService.getAdjAreas(areaId, distance), (count/300)*300)
		return CommunityListResponse(
			fetch.subList(0, min(15, fetch.size)).map {
				CommunitySummary(
					it.getId(),
					it.getTitle(),
					it.getRepimg(),
					it.getCreatedAt(),
					it.getViewCnt(),
					it.getLikeCnt(),
					it.getChatCnt(),
					it.getDescription(),
					it.getAreaInfo()
				)
			},
			fetch.getOrNull(fetch.size - 2)?.getEnd() ?: 0L,
			seed,
			fetch.size != 16,
			count + fetch.subList(0, min(15, fetch.size)).size
		)
	}
	override fun findCommunityById(id: Long): Community {
		val community: CommunityEntity = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		community.viewCnt++
		communityRepository.save(community)
		return Community(community)
	}

	@Transactional
	override fun create(communityRequest: CommunityRequest, authUserInfo: AuthUserInfo) {
		val userId = authUserInfo.uid
		val user = userRepository.findById(userId).getOrNull() ?: throw BaniException(ErrorType.UNAUTHORIZED)
		if (communityRequest.areaId !in authUserInfo.refAreaIds) {
			throw BaniException(ErrorType.INVALID_PARAMETER)
		}
		val community = CommunityEntity(
			author = user,
			areaInfo = areaService.getAreaById(communityRequest.areaId),
			createdAt = Instant.now(),
			title = communityRequest.title,
			description = communityRequest.description,
			viewCnt = 0,
			likeCnt = 0,
			chatCnt = 0,
			repImg = communityRequest.repImg,
		)
		val imageLists = communityRequest.images.map {
			CommunityImageEntity(url = it, community = community)
		}
		community.images = imageLists
		communityImageRepository.saveAll(imageLists)
		communityRepository.save(community)
	}

	@Transactional
	override fun update(communityRequest: CommunityUpdateRequest, userId: Long, id: Long) {
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

	override fun getCommentList(userId: Long, id: Long): List<CommentListResponse> {
		val comments = commentRepository.findByCommunityId(id)
		val response = comments.map {
			val childComments = commentRepository.findByParentId(it.id).map{
				CommentSummary(
					it.id,
					it.author.nickname,
					it.comment,
					it.imgUrl,
					it.createdAt,
					it.likeCnt,
				)
			}

			CommentListResponse(
				it.id,
				it.author.nickname,
				it.comment,
				it.imgUrl,
				it.createdAt,
				it.likeCnt,
				childComments
			)
		}
		return response
	}

	@Transactional
	override fun createComment(commentRequest: CommentRequest, userId: Long, id: Long) {
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
	override fun updateComment(commentUpdateRequest: CommentUpdateRequest, userId: Long, id: Long, commentId: Long) {
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
