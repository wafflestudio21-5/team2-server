package com.wafflestudio.team2server.post.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.post.model.*
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
		@RequestParam(required = true) areaId: Int,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	): ListResponse {
		val seed = when (seed) {
			0 -> Random.nextInt().absoluteValue
			else -> seed
		}
		return productPostService.getPostListRandom(cur, seed, distance, count, areaId, authUserInfo)
	}

	@PostMapping("/posts")
	fun postProduct(
		@RequestBody postCreateRequest: PostCreateRequest,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		productPostService.create(postCreateRequest, authUserInfo)
	}

	@GetMapping("/posts/{id}")
	fun getPost(
		@PathVariable id: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	): ProductPost {
		return productPostService.getPostById(id, authUserInfo)
	}

	@PostMapping("/posts/wish/{id}")
	fun wishPost(
		@PathVariable id: Long,
		@RequestParam(required = false, defaultValue = "true") enable: Boolean,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		if (enable) productPostService.likePost(authUserInfo.uid, id)
		else productPostService.unlikePost(authUserInfo.uid, id)
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
		val target = productPostService.getPostById(id, authUserInfo)
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
		@RequestParam(required = false, defaultValue = "1") distance: Int,
		@RequestParam(required = false, defaultValue = Long.MAX_VALUE.toString()) cur: Long,
		@RequestParam(required = false, defaultValue = "0") count: Int,
		@RequestParam(required = true) areaId: Int,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
	): ListResponse {
		return productPostService.searchPostByKeyword(cur, keyword, distance, count, areaId, authUserInfo)
	}


}
