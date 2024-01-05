package com.wafflestudio.team2server.channel.controller

import com.wafflestudio.team2server.channel.service.*
import com.wafflestudio.team2server.common.auth.AuthUserInfo
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/channels")
class ChannelController(
	private val channelService: ChannelService,
) {

	@GetMapping("")
	fun getMyChannels(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
	): ChannelListResponse {
		return channelService.getList(authUserInfo.uid)
	}

	@GetMapping("/{channelId}")
	fun getChannelDetails(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@PathVariable channelId: Long,
		@RequestParam(required = false, defaultValue = "30") size: Int,
		@RequestParam(required = false) firstNo: Long?,
		@RequestParam(required = false, defaultValue = "false") channelInfo: Boolean
	) {
		TODO()
	}

	@PostMapping("")
	fun createChannel(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@RequestBody req: CreateChannelRequest
	): CreateChannelResponse {
		return channelService.createChannel(authUserInfo.uid, req.postId)
	}

	@PostMapping("/{channelId}/pin")
	fun pin(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@PathVariable channelId: Long,
	): PinResponse {
		return channelService.pin(authUserInfo.uid, channelId)
	}

	@DeleteMapping("/{channelId}/pin")
	fun unpin(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@PathVariable channelId: Long,
	): UnpinResponse {
		return channelService.unpin(authUserInfo.uid, channelId)
	}

	@DeleteMapping("/{channelId}")
	fun exit(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@PathVariable channelId: Long,
	) {
		TODO()
	}

	data class CreateChannelRequest(
		val postId: Long
	)
}

