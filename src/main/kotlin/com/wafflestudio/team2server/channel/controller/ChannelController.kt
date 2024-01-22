package com.wafflestudio.team2server.channel.controller

import com.wafflestudio.team2server.channel.service.*
import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.common.error.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/channels")
@Tag(name = "채팅 관련 REST API")
class ChannelController(
	private val channelService: ChannelService,
) {

	@Operation(
		summary = "나의 채팅 목록 조회", description = "API 요청자가 속해있는 채팅방들의 요약된 정보를 리스트로 조회한다.",
		responses = [
			ApiResponse(responseCode = "200", description = "조회 성공"),
		])
	@GetMapping("")
	fun getMyChannels(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
	): ChannelListResponse {
		return channelService.getList(authUserInfo.uid)
	}
	@Operation(hidden = true,
		summary = "채팅 상세 조회(무한 스크롤)", description = "ID에 해당하는 채팅방의 상세 정보를 제공한다.",
		responses = [
			ApiResponse(responseCode = "200", description = "조회 성공"),
		])
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

	@Operation(
		summary = "판매자와의 채팅방 생성", description = "API 요청자와 판매자 간의 채팅방을 생성한 뒤, 해당 채팅방 ID를 반환한다. 이미 존재하는 채팅방이 있다면, 생성하지 않고 기존 채팅방 ID를 반환한다.",
		responses = [
			ApiResponse(responseCode = "200", description = "채팅방 생성 성공"),
			ApiResponse(responseCode = "404", description = "14001, POST_ID_NOT_FOUND", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
			ApiResponse(responseCode = "409", description = "19006, SELF_TRANSACTION", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
		]
	)
	@PostMapping("")
	fun createChannel(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@RequestBody req: ChannelCreateRequest
	): ChannelCreateResponse {
		return channelService.createChannel(authUserInfo.uid, req.postId)
	}

	@Operation(
		summary = "채팅방 상단 고정", description = "채팅방을 상단 고정한다. 고정 채팅방의 순서는 고정 시각의 내림차순이다.",
		responses = [
			ApiResponse(responseCode = "200", description = "상단 고정 성공"),
			ApiResponse(responseCode = "404", description = "14002, CHANNEL_USER_ID_NOT_FOUND", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
			ApiResponse(responseCode = "409", description = "19004, ALREADY_PINNED", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
		]
	)
	@PostMapping("/{channelId}/pin")
	fun pin(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@PathVariable channelId: Long,
	): ChannelPinResponse {
		return channelService.pin(authUserInfo.uid, channelId)
	}

	@Operation(
		summary = "채팅방 상단 고정 해제", description = "채팅방의 상단 고정을 해제한다.",
		responses = [
			ApiResponse(responseCode = "200", description = "상단 고정 해제 성공"),
			ApiResponse(responseCode = "404", description = "14002, CHANNEL_USER_ID_NOT_FOUND", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
			ApiResponse(responseCode = "409", description = "19005, NOT_PINNED", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
		]
	)
	@DeleteMapping("/{channelId}/pin")
	fun unpin(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@PathVariable channelId: Long,
	): ChannelUnpinResponse {
		return channelService.unpin(authUserInfo.uid, channelId)
	}

	@Operation(hidden = true,
		summary = "채팅방 나가기", description = "API 요청자가 해당 채팅방을 나간다.",
		responses = [
			ApiResponse(responseCode = "200", description = "채팅방 나가기 성공"),
//			ApiResponse(responseCode = "404", description = "14002, CHANNEL_USER_ID_NOT_FOUND", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
//			ApiResponse(responseCode = "409", description = "19005, NOT_PINNED", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
		]
	)
	@DeleteMapping("/{channelId}")
	fun exit(
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
		@PathVariable channelId: Long,
	) {
		TODO()
	}

	@Schema(description = "채팅방 생성 요청 DTO")
	data class ChannelCreateRequest(
		@Schema(description = "물품 게시글 식별자(ID)")
		val postId: Long
	)
}

