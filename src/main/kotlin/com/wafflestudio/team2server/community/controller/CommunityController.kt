package com.wafflestudio.team2server.community.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.community.service.CommunityService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

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
//		@RequestBody
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		TODO("authUserInfo.uid")
	}

	@GetMapping("/{postId}")
	fun getCommunity(@PathVariable postId: Long) {
		TODO()
	}

	@PutMapping("/{postId}")
	fun updateCommunity(@PathVariable postId: Long) {
		TODO("구현")
	}

	@DeleteMapping("/{postId}")
	fun deleteCommunity(
		@PathVariable postId: Long,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		TODO("구현")
	}

	data class PostCreateRequest(
		val title: String = "",
		val description: String = "",
	)

	data class PostUpdateRequest(
		val title: String?,
		val description: String?,
		val status: String?,
	)

}
