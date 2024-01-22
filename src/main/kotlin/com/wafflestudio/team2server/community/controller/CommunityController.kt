package com.wafflestudio.team2server.community.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.community.model.*
import com.wafflestudio.team2server.community.service.CommunityService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import kotlin.math.absoluteValue
import kotlin.random.Random

@RestController
@RequestMapping("/community")
@Tag(name = "커뮤니티 관련 REST API")
class CommunityController(private val communityService: CommunityService) {
	@GetMapping("")
	fun getCommunityList(
		@RequestParam(required = false, defaultValue = Long.MAX_VALUE.toString()) cur: Long,
		@RequestParam(required = false, defaultValue = "0") seed: Int,
		@RequestParam(required = false, defaultValue = "1") distance: Int,
		@RequestParam(required = false, defaultValue = "0") count: Int,
		@RequestParam(required = true) areaId: Int,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	): CommunityListResponse {
		val seed = when (seed) {
			0 -> Random.nextInt().absoluteValue
			else -> seed
		}
		return communityService.getCommunityList(cur, seed, distance, count, areaId, authUserInfo)
	}

	@PostMapping("")
	fun postCommunity(
		@RequestBody communityRequest: CommunityRequest,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		communityService.create(communityRequest, authUserInfo.uid)
	}

	@GetMapping("/{communityId}")
	fun getCommunity(@PathVariable communityId: Long): Community {
		return communityService.findCommunityById(communityId)
	}

	@PutMapping("/{communityId}")
	fun updateCommunity(
		@PathVariable communityId: Long,
		@RequestBody communityRequest: CommunityUpdateRequest,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
	) {
		communityService.update(communityRequest, authUserInfo.uid, communityId)
	}

	@DeleteMapping("/{communityId}")
	fun deleteCommunity(
		@PathVariable communityId: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		communityService.delete(authUserInfo.uid, communityId)
	}

	@PostMapping("/{communityId}")
	fun likeCommunity (
		@PathVariable communityId: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		communityService.likeCommunity(authUserInfo.uid, communityId)
	}

	@GetMapping("/{communityId}/chat")
	fun getCommentList(
		@PathVariable communityId: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	): List<CommentListResponse> {
		return communityService.getCommentList(authUserInfo.uid, communityId)
	}

	@PostMapping("/{communityId}/comment")
	fun postComment(
		@PathVariable communityId: Long,
		@RequestBody commentRequest: CommentRequest,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		communityService.createComment(commentRequest, authUserInfo.uid, communityId)
	}

	@PutMapping("/{communityId}/{commentId}")
	fun updateComment(
		@PathVariable communityId: Long,
		@PathVariable commentId: Long,
		@RequestBody commentUpdateRequest: CommentUpdateRequest,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		communityService.updateComment(commentUpdateRequest, authUserInfo.uid, communityId, commentId)
	}

	@DeleteMapping("/{communityId}/{commentId}")
	fun deleteComment(
		@PathVariable communityId: Long,
		@PathVariable commentId: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		communityService.deleteComment(authUserInfo.uid, communityId, commentId)
	}

	@PostMapping("/{communityId}/{commentId}")
	fun likeComment (
		@PathVariable communityId: Long,
		@PathVariable commentId: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		communityService.likeComment(authUserInfo.uid, communityId, commentId)
	}


}
