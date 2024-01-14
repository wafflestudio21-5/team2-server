package com.wafflestudio.team2server.community.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.community.model.Community
import com.wafflestudio.team2server.community.service.CommunityService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/community")
@Tag(name = "커뮤니티 관련 REST API")
class CommunityController(private val communityService: CommunityService) {
	@GetMapping("")
	fun getCommunityList() {
		TODO("구현")
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

	data class CommunityRequest(
		val title: String = "",
		val description: String = "",
	)
	data class CommunityUpdateRequest(
		val title: String?,
		val description: String?,
	)
}
