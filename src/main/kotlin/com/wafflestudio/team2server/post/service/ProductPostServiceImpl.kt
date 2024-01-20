package com.wafflestudio.team2server.post.service

import com.wafflestudio.team2server.area.service.AreaService
import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.common.error.UserNotFoundException
import com.wafflestudio.team2server.post.model.*
import com.wafflestudio.team2server.post.repository.*
import com.wafflestudio.team2server.post.repository.AuctionRepository.Companion.DIGIT
import com.wafflestudio.team2server.user.model.User
import com.wafflestudio.team2server.user.repository.UserRepository
import com.wafflestudio.team2server.user.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import kotlin.jvm.optionals.getOrNull
import kotlin.math.min

@Service
class ProductPostServiceImpl(
	private val productPostRepository: ProductPostRepository,
	private val areaService: AreaService,
	private val userService: UserService,
	private val userRepository: UserRepository,
	private val wishListRepository: WishListRepository,
	private val productPostImageRepository: ProductPostImageRepository,
	private val auctionRepository: AuctionRepository,
) : ProductPostService {
	override fun exists(id: Long): Boolean {
		return productPostRepository.findById(id).getOrNull() != null
	}

	@Transactional
	override fun create(postCreateRequest: PostCreateRequest, authUserInfo: AuthUserInfo) {
		val userId = authUserInfo.uid
		if (postCreateRequest.areaId !in authUserInfo.refAreaIds) {
			throw BaniException(ErrorType.INVALID_PARAMETER)
		}
		val type = when (postCreateRequest.type) {
			"TRADE" -> ProductPost.ProductPostType.TRADE
			"SHARE" -> ProductPost.ProductPostType.SHARE
			"AUCTION" -> ProductPost.ProductPostType.AUCTION
			else -> throw BaniException(ErrorType.INVALID_PARAMETER)
		}
		if (type == ProductPost.ProductPostType.AUCTION && postCreateRequest.deadline == null) {
			throw BaniException(ErrorType.INVALID_PARAMETER)
		}
		val postEntity =
			ProductPostEntity(
				title = postCreateRequest.title,
				description = postCreateRequest.description,
				type = type,
				deadline = Instant.ofEpochMilli(postCreateRequest.deadline ?: 0),
				author = userRepository.findById(userId)
					.getOrNull() ?: throw BaniException(ErrorType.USER_NOT_FOUND),
				buyerId = -1,
				createdAt = Instant.now(),
				hiddenYn = false,
				status = ProductPost.ProductPostStatus.NEW,
				sellingArea = areaService.getAreaById(postCreateRequest.areaId),
				repImg = postCreateRequest.repImg,
				offerYn = postCreateRequest.offerYn,
				refreshCnt = 0,
				refreshedAt = Instant.now(),
				wishCnt = 0,
				chatCnt = 0,
				sellPrice = postCreateRequest.sellPrice
			)
		val postImageEntityList = postCreateRequest.images.map {
			ProductPostImageEntity(url = it, productPost = postEntity)
		}
		postEntity.images = postImageEntityList
		productPostImageRepository.saveAll(postImageEntityList)
		productPostRepository.save(postEntity)
	}

	@Transactional
	override fun update(postUpdateRequest: PostUpdateRequest, userId: Long, id: Long, refresh: Boolean) {
		val target = productPostRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.POST_NOT_FOUND)
		if (target.author.id != userId) {
			throw BaniException(ErrorType.UNAUTHORIZED)
		}
		if (refresh) {
			target.refreshedAt = Instant.now()
			target.refreshCnt += 1
		}
		target.title = postUpdateRequest.title ?: target.title
		target.type = when (postUpdateRequest.type) {
			"TRADE" -> ProductPost.ProductPostType.TRADE
			"SHARE" -> ProductPost.ProductPostType.SHARE
			"AUCTION" -> {
				if (target.deadline.toEpochMilli() == 0L || postUpdateRequest.deadline == null) {
					throw BaniException(ErrorType.INVALID_PARAMETER)
				}
				ProductPost.ProductPostType.AUCTION
			}
			else -> target.type
		}
		target.deadline = postUpdateRequest.deadline?.let { Instant.ofEpochMilli(it) } ?: target.deadline
		target.description = postUpdateRequest.description ?: target.description
		target.hiddenYn = postUpdateRequest.hiddenYn ?: target.hiddenYn
		target.status = when (postUpdateRequest.status) {
			"NEW" -> ProductPost.ProductPostStatus.NEW
			"SOLDOUT" -> ProductPost.ProductPostStatus.SOLDOUT
			"RESERVED" -> ProductPost.ProductPostStatus.RESERVED
			else -> target.status
		}
		target.offerYn = postUpdateRequest.offerYn ?: target.offerYn
		target.sellPrice = postUpdateRequest.sellPrice ?: target.sellPrice
		target.repImg = postUpdateRequest.repImg ?: target.repImg
		target.images = postUpdateRequest.images?.map {
			val image = ProductPostImageEntity(url = it, productPost = target)
			productPostImageRepository.save(image)
			image
		} ?: target.images
		productPostRepository.save(target)
	}

	override fun searchPostByKeyword(
		cur: Long,
		keyword: String,
		distance: Int,
		count: Int,
		areaId: Int,
		authUserInfo: AuthUserInfo
	): ListResponse {
		if (areaId !in authUserInfo.refAreaIds) {
			throw BaniException(ErrorType.INVALID_PARAMETER)
		}
		val adjAreaIdList = areaService.getAdjAreas(areaId, distance)
		val fetch = productPostRepository.findByKeywordIgnoreCaseAndSellingArea(cur, keyword, adjAreaIdList)
		return ListResponse(
			fetch.subList(0, min(15, fetch.size)).map {
				PostSummary(
					id = it.id ?: throw BaniException(ErrorType.POST_NOT_FOUND),
					title = it.title,
					repImg = it.repImg,
					createdAt = it.createdAt.toEpochMilli(),
					refreshedAt = it.refreshedAt.toEpochMilli(),
					chatCnt = it.chatCnt,
					wishCnt = it.wishCnt,
					sellPrice = it.sellPrice,
					sellingArea = areaService.getAreaById(it.sellingArea.id).name,
					deadline = it.deadline.toEpochMilli(),
					type = it.type.name,
					status = it.status.name,
				)
			},
			fetch.getOrNull(fetch.size - 2)?.id ?: 0L,
			null,
			fetch.size != 16,
			count + fetch.subList(0, min(15, fetch.size)).size
		)
	}

	override fun getPostById(id: Long, authUserInfo: AuthUserInfo): ProductPost {
		val userId = authUserInfo.uid
		val postEntity: ProductPostEntity = productPostRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.POST_NOT_FOUND)
		if (postEntity.hiddenYn && postEntity.author.id != userId) {
			throw BaniException(ErrorType.POST_NOT_FOUND)
		}
		// TODO: type이 auction 인 경우 최고가 포함해서 보내기
		return ProductPost(postEntity, authUserInfo) ?: throw BaniException(ErrorType.POST_NOT_FOUND)
	}

	@Transactional
	override fun deleteById(id: Long) {
		productPostRepository.deleteById(id)
		wishListRepository.findByPostId(id).forEach(wishListRepository::delete)
		productPostImageRepository.findByProductPostId(id).forEach(productPostImageRepository::delete)
	}

	@Transactional
	override fun likePost(userId: Long, postId: Long) {
		if (!wishListRepository.existsByUserIdAndPostId(userId, postId)) {
			val wishListEntity = WishListEntity(userId = userId, postId = postId)
			wishListRepository.save(wishListEntity)
		}
	}

	@Transactional
	override fun unlikePost(userId: Long, postId: Long) {
		if (wishListRepository.existsByUserIdAndPostId(userId, postId)) {
			val wishListEntity = wishListRepository.findByUserIdAndPostId(userId = userId, postId = postId)
			wishListRepository.delete(wishListEntity)
		}
	}

	override fun getLikedPosts(userId: Long): List<PostSummary> {
		return wishListRepository.findByUserId(userId).mapNotNull {
			val post: ProductPostEntity? = productPostRepository.findById(it.postId).getOrNull()
			if (post == null) {
				wishListRepository.delete(it)
				return@mapNotNull null
			} else {
				PostSummary(
					id = post.id!!,
					title = post.title,
					repImg = post.repImg,
					createdAt = post.createdAt.toEpochMilli(),
					refreshedAt = post.refreshedAt.toEpochMilli(),
					chatCnt = post.chatCnt,
					wishCnt = post.wishCnt,
					sellPrice = post.sellPrice,
					sellingArea = areaService.getAreaById(post.sellingArea.id).name,
					deadline = post.deadline.toEpochMilli(),
					type = post.type.name,
					status = post.status.name,
				)
			}
		}
	}

	override fun getLikedUsers(postId: Long): List<User> {
		if (!productPostRepository.existsById(postId)) {
			throw BaniException(ErrorType.POST_NOT_FOUND)
		}
		return wishListRepository.findByPostId(postId).mapNotNull {
			try {
				userService.getUser(it.userId)
			} catch (e: UserNotFoundException) {
				null
			}
		}
	}

	override fun getPostListRandom(
		cur: Long,
		seed: Int,
		distance: Int,
		count: Int,
		areaId: Int,
		authUserInfo: AuthUserInfo
	): ListResponse {
		if (areaId !in authUserInfo.refAreaIds) {
			throw BaniException(ErrorType.INVALID_PARAMETER)
		}
		val cur = if (count % 300 == 0) Long.MAX_VALUE else cur
		val fetch = productPostRepository.findRandom(cur, seed, areaService.getAdjAreas(areaId, distance), (count / 300) * 300)
		return ListResponse(
			fetch.subList(0, min(15, fetch.size)).map {
				PostSummary(
					it.getId(),
					it.getTitle(),
					it.getRep_img(),
					it.getCreated_at().toEpochMilli(),
					it.getRefreshed_at()?.toEpochMilli() ?: 0L,
					it.getChat_cnt(),
					it.getWish_cnt(),
					it.getSell_price(),
					areaService.getAreaById(it.getSelling_area_id()).name,
					it.getDeadline().toEpochMilli(),
					it.getType(),
					it.getStatus(),
				)
			},
			fetch.getOrNull(fetch.size - 2)?.getEnd() ?: 0L,
			seed,
			fetch.size != 16,
			count + fetch.subList(0, min(15, fetch.size)).size
		)
	}

	override fun getAuctionPosts(userId: Long): List<BidSummary> {
		return auctionRepository.getAuctionPosts(userId)
			.mapNotNull {
				val post: ProductPostEntity = productPostRepository.findById(it).getOrNull() ?: return@mapNotNull null
				BidSummary(
					id = post.id!!,
					title = post.title,
					repImg = post.repImg,
					createdAt = post.createdAt.toEpochMilli(),
					refreshedAt = post.refreshedAt.toEpochMilli(),
					chatCnt = post.chatCnt,
					wishCnt = post.wishCnt,
					sellPrice = post.sellPrice,
					sellingArea = areaService.getAreaById(post.sellingArea.id).name,
					deadline = post.deadline.toEpochMilli(),
					type = post.type.name,
					status = post.status.name,
				)
			}
	}

	override fun bidList(postId: Long, authUserInfo: AuthUserInfo): List<BidInfo> {
		val postEntity: ProductPostEntity = productPostRepository.findById(postId).getOrNull() ?: throw BaniException(ErrorType.POST_NOT_FOUND)
		if (postEntity.hiddenYn && postEntity.author.id != authUserInfo.uid) {
			throw BaniException(ErrorType.POST_NOT_FOUND)
		}
		if (postEntity.type != ProductPost.ProductPostType.AUCTION) { // 경매 타입인지 체크
			throw BaniException(ErrorType.POST_NOT_FOUND)
		}
		return auctionRepository.getBidTop10(postId)
			.map {
				val userEntity = userRepository.findById(it.key).getOrNull() ?: throw UserNotFoundException
				BidInfo(userEntity.id, userEntity.nickname, it.value)
			}
	}

	override fun bid(userId: Long, id: Long, bidPrice: Int, now: Instant) {
		// 경매 타입인지 체크
		val postEntity: ProductPostEntity = productPostRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.POST_NOT_FOUND)
		if (postEntity.hiddenYn && postEntity.author.id != userId) {
			throw BaniException(ErrorType.POST_NOT_FOUND)
		}
		if (postEntity.type != ProductPost.ProductPostType.AUCTION) {
			throw BaniException(ErrorType.POST_NOT_FOUND)
		}
		// 경매 deadline 지났는지 여부
		if (now.isAfter(postEntity.deadline)) {
			throw BaniException(ErrorType.EXPIRED_AUCTION)
		}
		// 제시 가격이 최소 가격보다 낮을 때
		if (bidPrice < postEntity.sellPrice) {
			throw BaniException(ErrorType.INVALID_BID_PRICE)
		}
		// score 계산
		val score = calculateScore(bidPrice, now)
		// bidding
		auctionRepository.bid(id, userId, score)
	}

	private fun calculateScore(bidPrice: Int, now: Instant): Double {
		val epochMilli = now.toEpochMilli()
		val timeScore = DIGIT - epochMilli * 100 % DIGIT
		val bidScore = bidPrice.toDouble() * DIGIT
		return bidScore + timeScore
	}

	fun ProductPost(it: ProductPostEntity?, authUserInfo: AuthUserInfo): ProductPost? {
		if (it == null) return null
		return ProductPost(
			id = it.id ?: throw BaniException(ErrorType.POST_NOT_FOUND),
			authorId = it.author.id,
			authorName = it.author.nickname,
			buyerId = it.buyerId,
			chatCnt = it.chatCnt,
			sellPrice = it.sellPrice,
			createdAt = it.createdAt.toEpochMilli(),
			deadline = it.deadline.toEpochMilli(),
			offerYn = it.offerYn,
			refreshCnt = it.refreshCnt,
			refreshedAt = it.refreshedAt.toEpochMilli(),
			repImg = it.repImg,
			title = it.title,
			viewCnt = it.viewCnt,
			wishCnt = it.wishCnt,
			type = it.type.name,
			status = it.status.name,
			sellingArea = it.sellingArea.name,
			description = it.description,
			images = it.images.map { it.url },
			isWish = wishListRepository.existsByUserIdAndPostId(authUserInfo.uid, it.id),
			profileImg = userService.getUser(it.author.id).profileImageUrl ?: ""
		)
	}

}

