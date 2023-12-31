package com.wafflestudio.team2server.post.service

import com.wafflestudio.team2server.area.service.AreaService
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.post.controller.ProductPostController
import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.post.repository.ProductPostEntity
import com.wafflestudio.team2server.post.repository.ProductPostRepository
import com.wafflestudio.team2server.post.repository.WishListEntity
import com.wafflestudio.team2server.post.repository.WishListRepository
import com.wafflestudio.team2server.user.repository.UserEntity
import com.wafflestudio.team2server.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull
import kotlin.math.min

@Service
class ProductPostServiceImpl(
	private val productPostRepository: ProductPostRepository,
	private val areaService: AreaService,
	private val userRepository: UserRepository,
	private val wishListRepository: WishListRepository
) : ProductPostService {
	override fun exists(id: Long): Boolean {
		return productPostRepository.findById(id).getOrNull() != null
	}

	@Transactional
	override fun create(postCreateRequest: ProductPostController.PostCreateRequest, userId: Long) {
		val user = userRepository.findById(userId).getOrNull() ?: throw BaniException(ErrorType.UNAUTHORIZED)
		// upload images and get repimg urls...
		val postEntity =
			ProductPostEntity(
				title = postCreateRequest.title,
				description = postCreateRequest.description,
				type = when (postCreateRequest.type) {
					"TRADE" -> ProductPost.ProductPostType.TRADE
					"SHARE" -> ProductPost.ProductPostType.SHARE
					"AUCTION" -> ProductPost.ProductPostType.AUCTION
					else -> throw BaniException(ErrorType.INVALID_PARAMETER)
				},
				deadline = postCreateRequest.deadline,
				author = user,
				buyerId = -1,
				createdAt = LocalDateTime.now(),
				hiddenYn = postCreateRequest.hiddenYn,
				status = ProductPost.ProductPostStatus.NEW,
				sellingArea = areaService.getAreaById(0),
				repImg = "",
				offerYn = postCreateRequest.offerYn,
				refreshCnt = 0,
				refreshedAt = LocalDateTime.now(),
				wishCnt = 0,
				chatCnt = 0,
				sellPrice = postCreateRequest.sellPrice
			)
		productPostRepository.save(postEntity)
	}

	@Transactional
	override fun update(postUpdateRequest: ProductPostController.PostUpdateRequest, userId: Long, id: Long, refresh: Boolean) {
		val target = productPostRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.POST_NOT_FOUND)
		if (target.author.id != userId) {
			throw BaniException(ErrorType.UNAUTHORIZED)
		}
		if (refresh) {
			target.refreshedAt = LocalDateTime.now()
			target.refreshCnt += 1
		}
		target.title = postUpdateRequest.title ?: target.title
		target.type = when (postUpdateRequest.type) {
			"TRADE" -> ProductPost.ProductPostType.TRADE
			"SHARE" -> ProductPost.ProductPostType.SHARE
			"AUCTION" -> ProductPost.ProductPostType.AUCTION
			else -> target.type
		}
		target.deadline = postUpdateRequest.deadline ?: target.deadline
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
		productPostRepository.save(target)
	}

	override fun searchPostByKeyword(cur: Long, keyword: String, refAreaId: List<Int>, distance: Int): ProductPostController.ListResponse {
		val adjAreaIdList = areaService.getAdjAreas(refAreaId[0], distance)
		val fetch = productPostRepository.findByKeywordIgnoreCaseAndSellingArea(cur, keyword, adjAreaIdList)
		return ProductPostController.ListResponse(
			fetch.subList(0, min(15, fetch.size)).map {
				ProductPostController.PostSummary(
					id = it.id ?: throw BaniException(ErrorType.POST_NOT_FOUND),
					title = it.title,
					repImg = it.repImg,
					createdAt = it.createdAt,
					refreshedAt = it.refreshedAt,
					chatCnt = it.chatCnt,
					wishCnt = it.wishCnt,
					sellPrice = it.sellPrice,
					sellingArea = areaService.getAreaById(it.sellingArea.id).name,
					deadline = it.deadline,
					type = it.type.name,
					status = it.status.name,
				)
			},
			fetch.getOrNull(fetch.size - 2)?.id ?: 0L,
			null,
			fetch.size != 16
		)
	}

	override fun findPostById(id: Long): ProductPost {
		val postEntity: ProductPostEntity = productPostRepository.findById(id).getOrNull() ?: throw BaniException(ErrorType.POST_NOT_FOUND)
		return ProductPost(postEntity)
	}

	@Transactional
	override fun deleteById(id: Long) {
		productPostRepository.deleteById(id)
	}

	@Transactional
	override fun likePost(userId: Long, id: Long) {
		if (!wishListRepository.existsByUserIdAndPostId(userId, id)) {
			val wishListEntity = WishListEntity(userId = userId, postId = id, createdAt = LocalDateTime.now())
			wishListRepository.save(wishListEntity)
		}
	}

	@Transactional
	override fun unlikePost(userId: Long, id: Long) {
		if (wishListRepository.existsByUserIdAndPostId(userId, id)) {
			val wishListEntity = wishListRepository.findByUserIdAndPostId(userId = userId, postId = id)
			wishListRepository.delete(wishListEntity)
		}
	}

	override fun getLikedPosts(userId: Long): List<ProductPostEntity> {
		return wishListRepository.findByUserId(userId).mapNotNull { productPostRepository.findById(it.postId).getOrNull() }
	}

	override fun getLikedUsers(postId: Long): List<UserEntity> {
		return wishListRepository.findByPostId(postId).mapNotNull { userRepository.findById(it.userId).getOrNull() }
	}

	override fun getPostListRandom(cur: Long, seed: Int, areaId: Int, distance: Int): ProductPostController.ListResponse {
		val fetch = productPostRepository.findRandom(cur, seed, areaService.getAdjAreas(areaId, distance))
		return ProductPostController.ListResponse(
			fetch.subList(0, min(15, fetch.size)).map {
				ProductPostController.PostSummary(
					it.getId(),
					it.getTitle(),
					it.getRep_img(),
					it.getCreated_at(),
					it.getRefreshed_at(),
					it.getChat_cnt(),
					it.getWish_cnt(),
					it.getSell_price(),
					areaService.getAreaById(it.getSelling_area_id()).name,
					it.getDeadline(),
					it.getType(),
					it.getStatus(),
				)
			},
			fetch.getOrNull(fetch.size - 2)?.getEnd() ?: 0L,
			seed,
			fetch.size != 16
		)
	}

	fun ProductPost(it: ProductPostEntity): ProductPost {
		return ProductPost(
			id = it.id ?: throw BaniException(ErrorType.POST_NOT_FOUND),
			authorId = it.author.id,
			buyerId = it.buyerId,
			chatCnt = it.chatCnt,
			sellPrice = it.sellPrice,
			createdAt = it.createdAt,
			deadline = it.deadline,
			hiddenYn = it.hiddenYn,
			offerYn = it.offerYn,
			refreshCnt = it.refreshCnt,
			refreshedAt = it.refreshedAt,
			repImg = it.repImg,
			title = it.title,
			viewCnt = it.viewCnt,
			wishCnt = it.wishCnt,
			type = it.type.ordinal,
			status = it.status.ordinal,
			sellingArea = it.sellingArea.name
		)
	}
}

