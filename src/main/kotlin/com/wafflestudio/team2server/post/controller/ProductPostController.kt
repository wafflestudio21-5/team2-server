package com.wafflestudio.team2server.post.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.post.service.ProductPostService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import kotlin.math.absoluteValue
import kotlin.random.Random

@RestController
class ProductPostController(private val productPostService: ProductPostService) {
	@GetMapping("/posts")
	fun postList(
		@RequestParam(required = false, defaultValue = Long.MAX_VALUE.toString()) cur: Long,
		@RequestParam(required = false, defaultValue = "0") seed: Int,
		@RequestParam(required = false, defaultValue = "1") distance: Int,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	): ListResponse {
		val seed = when (seed) {
			0 -> Random.nextInt().absoluteValue
			else -> seed
		}
		val res = productPostService.getPostListRandom(cur, seed, authUserInfo.refAreaIds[0], distance)
		return res
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
	fun getPost(@PathVariable id: Long): ProductPost {
		return productPostService.findPostById(id)
	}

	@PostMapping("/posts/{id}")
	fun likePost(
		@PathVariable id: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {

	}

	@PutMapping("/posts/{id}")
	fun updatePost(
		@PathVariable id: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@RequestBody postUpdateRequest: PostUpdateRequest,
		@RequestParam refresh: Boolean
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
		val target = productPostService.findPostById(id)
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
		@RequestParam distance: Int
	): List<ProductPost> {
		return productPostService.searchPostByTitleAndArea(keyword, authUserInfo.refAreaIds, distance)
	}

	data class PostCreateRequest(
		val title: String = "",
		val description: String = "",
		val type: String = "NEW",
		val offerYn: Boolean = false,
		val deadline: LocalDateTime = LocalDateTime.now().plusDays(7),
		val hiddenYn: Boolean = false,
		val sellPrice: Int
	)

	data class PostUpdateRequest(
		val title: String?,
		val description: String?,
		val type: String?,
		val status: String?,
		val offerYn: Boolean?,
		val deadline: LocalDateTime?,
		val hiddenYn: Boolean?,
		val sellPrice: Int?,
	)

	data class PostSummary(
		val id: Long,
		val title: String,
		val repImg: String?,
		val createdAt: LocalDateTime,
		val refreshedAt: LocalDateTime?,
		val chatCnt: Int,
		val wishCnt: Int,
		val sellPrice: Int,
		val sellingArea: String,
		val deadline: LocalDateTime,
		val type: String,
		val status: String,
	)

	data class ListResponse(
		val data: List<PostSummary>,
		val cur: Long,
		val seed: Int,
		val isLast: Boolean
	)
}
