package com.wafflestudio.team2server.community.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.community.model.Community
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
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) : ListResponse {
		val seed = when (seed) {
			0 -> Random.nextInt().absoluteValue
			else -> seed
		}
		return communityService.getCommunityList(cur, seed, authUserInfo.refAreaIds[0], distance)
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

	data class CommunityRequest(
		val title: String = "",
		val description: String = "",
	)
	data class CommunityUpdateRequest(
		val title: String?,
		val description: String?,
	)

	data class CommentRequest(
		val comment: String = "",
		val parentId: Long
	)

	data class CommentUpdateRequest(
		val comment: String?
	)
	data class CommunitySummary(
		val id: Long,
		val title: String,
		val repImg: String,
		val createdAt: Long?,
		val viewCnt: Int,
		val likeCnt: Int,
		val chatCnt: Int,
		val description: String,
		val areaId: Long
	)
	data class ListResponse(
		val data: List<CommunitySummary>,
		val cur: Long,
		val seed: Int?,
		val isLast: Boolean
	)
}
