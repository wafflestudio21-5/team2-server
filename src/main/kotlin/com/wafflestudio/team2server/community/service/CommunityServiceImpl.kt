package com.wafflestudio.team2server.community.service;

import com.wafflestudio.team2server.area.service.AreaService
import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.community.model.*
import com.wafflestudio.team2server.community.repos.CommentImageEntity
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
	private val communityImageRepository: CommunityImageRepository,
	private val commentImageRepository: CommentImageRepository,
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
					it.getRep_img(),
					it.getCreated_at()?.toEpochMilli(),
					it.getView_cnt(),
					it.getLike_cnt(),
					it.getChat_cnt(),
					it.getDescription(),
					areaService.getAreaById(it.getArea_id()).name
				)
			},
			fetch.getOrNull(fetch.size - 2)?.getEnd() ?: 0L,
			seed,
			fetch.size != 16,
			count + fetch.subList(0, min(15, fetch.size)).size
		)
	}
	override fun findCommunityById(userId: Long, id: Long): CommunityResponse {
		val community: CommunityEntity = communityRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.COMMUNITY_NOT_FOUND)
		community.viewCnt++
		val response = CommunityResponse(
			community = Community(community),
			isLiked = communityLikeRepository.existsByUserIdAndCommunityId(userId, community.id!!),
			nickname = community.author.nickname,
			profileImg = community.author.profileImg,
			areaInfo = community.areaInfo.name
		)
		communityRepository.save(community)
		return response
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
		community.repImg = communityRequest.repImg ?: community.repImg
		community.images = communityRequest.images?.map {
			val image = CommunityImageEntity(url = it, community = community)
			communityImageRepository.save(image)
			image
		} ?: community.images
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
			val childComments = commentRepository.findByParentId(it.id).map{ cc ->
				CommentSummary(
					id = cc.id,
					nickname = cc.author.nickname,
					comment =cc.comment,
					imgUrl = cc.imgUrl,
					createdAt = cc.createdAt.toEpochMilli(),
					likeCnt = cc.likeCnt,
					isLiked = commentLikeRepository.existsByUserIdAndCommentId(userId, cc.id),
					images = cc.images.map {img -> img.url },
				)
			}

			CommentListResponse(
				id = it.id,
				nickname = it.author.nickname,
				comment = it.comment,
				imgUrl = it.imgUrl,
				createdAt = it.createdAt.toEpochMilli(),
				likeCnt = it.likeCnt,
				isLiked = commentLikeRepository.existsByUserIdAndCommentId(userId, it.id),
				childComments = childComments,
				images = it.images.map {img -> img.url }
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
			imgUrl = commentRequest.imgUrl,
			likeCnt = 0,
			createdAt = Instant.now(),
			updatedAt = Instant.now(),
		)
		community.chatCnt++
		val imageLists = commentRequest.images.map {
			CommentImageEntity(url = it, comment = comment)
		}
		comment.images = imageLists
		commentImageRepository.saveAll(imageLists)
		commentRepository.save(comment)
		communityRepository.save(community)
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
		comment.imgUrl = commentUpdateRequest.imgUrl ?: comment.imgUrl
		comment.images = commentUpdateRequest.images?.map {
			val image = CommentImageEntity(url = it, comment = comment)
			commentImageRepository.save(image)
			image
		} ?: comment.images
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
