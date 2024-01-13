package com.wafflestudio.team2server.post.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.post.service.ProductPostService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import kotlin.math.absoluteValue
import kotlin.random.Random

@RestController
class ProductPostController(private val productPostService: ProductPostService) {
	@GetMapping("/posts")
	fun postList(
		@RequestParam(required = false, defaultValue = Long.MAX_VALUE.toString()) cur: Long,
		@RequestParam(required = false, defaultValue = "0") seed: Int,
		@RequestParam(required = false, defaultValue = "1") distance: Int,
		@RequestParam(required = false, defaultValue = "0") count: Int,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	): ListResponse {
		val seed = when (seed) {
			0 -> Random.nextInt().absoluteValue
			else -> seed
		}
		return productPostService.getPostListRandom(cur, seed, authUserInfo.refAreaIds[0], distance, count)
	}

	@PostMapping("/posts")
	fun postProduct(
		@RequestBody postCreateRequest: PostCreateRequest,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		val userId: Long = authUserInfo.uid
		productPostService.create(postCreateRequest, userId)
	}

	@GetMapping("/posts/{id}")
	fun getPost(
		@PathVariable id: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	): ProductPost {
		return productPostService.getPostById(id, authUserInfo.uid)
	}

	@PostMapping("/posts/wish/{id}")
	fun wishPost(
		@PathVariable id: Long,
		@RequestParam(required = false, defaultValue = "true") enable: Boolean,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		if (enable) productPostService.likePost(id, authUserInfo.uid)
		else productPostService.unlikePost(id, authUserInfo.uid)
	}

	@GetMapping("/posts/wish")
	fun getWishList(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	): List<PostSummary> {
		return productPostService.getLikedPosts(authUserInfo.uid)
	}

	@PutMapping("/posts/{id}")
	fun updatePost(
		@PathVariable id: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@RequestBody postUpdateRequest: PostUpdateRequest,
		@RequestParam(required = false, defaultValue = "false") refresh: Boolean
	) {
		productPostService.update(
			postUpdateRequest,
			authUserInfo.uid,
			id,
			refresh = refresh
		)
	}


	@DeleteMapping("/posts/{id}")
	fun deletePost(
		@PathVariable id: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		val target = productPostService.getPostById(id, authUserInfo.uid)
		if (authUserInfo.uid != target.authorId) {
			throw BaniException(ErrorType.PERMISSION_DENIED)
		} else if (!productPostService.exists(id)) {
			throw BaniException(ErrorType.POST_NOT_FOUND)
		} else {
			productPostService.deleteById(id)
		}
	}

	@GetMapping("/posts/search")
	fun searchPost(
		@RequestParam keyword: String,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@RequestParam(required = false, defaultValue = "1") distance: Int,
		@RequestParam(required = false, defaultValue = Long.MAX_VALUE.toString()) cur: Long,
		@RequestParam(required = false, defaultValue = "0") count: Int,
	): ListResponse {
		return productPostService.searchPostByKeyword(cur, keyword, authUserInfo.refAreaIds, distance, count)
	}

	data class PostCreateRequest(
		val title: String = "",
		val description: String = "",
		val type: String = "NEW",
		val repImg: String = "",
		val images: List<String> = listOf(),
		val offerYn: Boolean = false,
		val deadline: Long? = null,
		val sellPrice: Int
	)


	data class PostUpdateRequest(
		val title: String? = null,
		val description: String? = null,
		val type: String? = null,
		val repImg: String? = null,
		val images: List<String>? = null,
		val status: String? = null,
		val offerYn: Boolean? = null,
		val deadline: Long? = null,
		val hiddenYn: Boolean? = null,
		val sellPrice: Int? = null,
	)

	data class PostSummary(
		val id: Long,
		val title: String,
		val repImg: String,
		val createdAt: Long?,
		val refreshedAt: Long?,
		val chatCnt: Int,
		val wishCnt: Int,
		val sellPrice: Int,
		val sellingArea: String,
		val deadline: Long?,
		val type: String,
		val status: String,
	)

	data class ListResponse(
		val data: List<PostSummary>,
		val cur: Long,
		val seed: Int?,
		val isLast: Boolean,
		val count: Int,
	)
}
