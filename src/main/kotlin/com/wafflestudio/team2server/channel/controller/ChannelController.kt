package com.wafflestudio.team2server.channel.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/channels")
class ChannelController {

	@GetMapping("")
	fun getMyChannels() {
		TODO()

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
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		TODO()
	}

	@PostMapping("/{channelId}/pin")
	fun pin(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@PathVariable channelId: Long,
	) {
		TODO()
	}

	@DeleteMapping("/{channelId}/pin")
	fun unpin(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@PathVariable channelId: Long,
	) {
		TODO()
	}

//	@ExceptionHandler
//	fun handleException(e: )
}
