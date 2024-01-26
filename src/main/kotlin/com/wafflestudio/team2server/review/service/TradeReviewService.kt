package com.wafflestudio.team2server.review.service

import com.wafflestudio.team2server.area.service.AreaService
import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.common.error.PostNotFoundException
import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.post.repository.ProductPostRepository
import com.wafflestudio.team2server.post.service.ProductPostService
import com.wafflestudio.team2server.review.model.TradeReview
import com.wafflestudio.team2server.review.model.TradeReviewRequest
import com.wafflestudio.team2server.review.repository.TradeReviewEntity
import com.wafflestudio.team2server.review.repository.TradeReviewRepository
import com.wafflestudio.team2server.user.repository.UserEntity
import com.wafflestudio.team2server.user.repository.UserRepository
import com.wafflestudio.team2server.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class TradeReviewService(
	private val userRepository: UserRepository,
	private val productPostService: ProductPostService,
	private val tradeReviewRepository: TradeReviewRepository,
	private val areaService: AreaService,
	private val productPostRepository: ProductPostRepository
) {
	@Transactional
	fun createTradeReview(postId: Long, request: TradeReviewRequest, authUserInfo: AuthUserInfo) {
		val sender: UserEntity = userRepository.findById(authUserInfo.uid).getOrElse { throw BaniException(ErrorType.USER_NOT_FOUND) }
		val receiver = userRepository.findById(request.receiverId).getOrElse { throw BaniException(ErrorType.USER_NOT_FOUND) }
		val post = productPostRepository.findById(postId).getOrElse { throw PostNotFoundException }
		val authorArea = if (request.authorAreaId in authUserInfo.refAreaIds) { // 작성자 지역 체크
			areaService.getAreaById(request.authorAreaId)
		} else {
			throw BaniException(ErrorType.INVALID_PARAMETER)
		}
		val authorType = when (authUserInfo.uid) {
			post.author.id -> TradeReviewEntity.AuthorType.SELLER
			else -> TradeReviewEntity.AuthorType.BUYER
		}
		if (tradeReviewRepository.existsByPostAndAuthorType(post, authorType)) { // 이미 리뷰를 작성한 경우 에러
			throw BaniException(ErrorType.ALREADY_REVIEWED)
		}
		val tradeReview = TradeReviewEntity(
			post = post,
			sender = sender,
			receiver = receiver,
			authorType = authorType,
			authorArea = authorArea,
		)
		if (post.status != ProductPost.ProductPostStatus.SOLDOUT) { // 거래 종료 및 구매자 ID 등록
			post.buyerId = when (authUserInfo.uid) {
				post.author.id -> request.receiverId
				else -> authUserInfo.uid
			}
			post.status = ProductPost.ProductPostStatus.SOLDOUT
		}
		tradeReviewRepository.save(tradeReview)
		println(request.getEval().delta)
		receiver.mannerTemperature += request.getEval().delta
		println(receiver.mannerTemperature)
	}

	fun getTradeReviewFrom(userId: Long, from: String): List<TradeReview> {
		return when (from) {
			"buyer" -> tradeReviewRepository.findAllByReceiverIdAndAuthorType(userId, TradeReviewEntity.AuthorType.BUYER)
				.mapNotNull { toTradeReview(it) }.sortedBy { it.createdAt.dec() }
			"seller" -> tradeReviewRepository.findAllByReceiverIdAndAuthorType(userId, TradeReviewEntity.AuthorType.SELLER)
				.mapNotNull { toTradeReview(it) }.sortedBy { it.createdAt.dec() }
			"all" -> (tradeReviewRepository.findAllByReceiverIdAndAuthorType(userId, TradeReviewEntity.AuthorType.BUYER)
				.mapNotNull { toTradeReview(it) } + tradeReviewRepository.findAllByReceiverIdAndAuthorType(userId, TradeReviewEntity.AuthorType.SELLER)
				.mapNotNull { toTradeReview(it) }).sortedBy { it.createdAt.dec() }
			else -> throw BaniException(ErrorType.INVALID_PARAMETER)
		}
	}

	fun toTradeReview(tradeReviewEntity: TradeReviewEntity?): TradeReview? {
		if (tradeReviewEntity == null) {
			return null
		}
		return TradeReview(
			id = tradeReviewEntity.id,
			senderId = tradeReviewEntity.sender!!.id,
			senderName = tradeReviewEntity.sender.nickname,
			senderAreaName = tradeReviewEntity.authorArea!!.name,
			senderProfileImg = tradeReviewEntity.sender.profileImg!!,
			createdAt = tradeReviewEntity.createdAt!!.toEpochMilli(),
			description = tradeReviewEntity.description ?: "",
		)
	}
}
