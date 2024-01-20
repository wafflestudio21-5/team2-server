package com.wafflestudio.team2server.channel.service

import com.wafflestudio.team2server.channel.repository.ChannelUserRepository
import com.wafflestudio.team2server.common.auth.TokenVerifier
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.common.util.MessageUtil
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler


private val logger: KLogger = KotlinLogging.logger { }

@Component
class ChannelDetailHandler(
	private val messageUtil: MessageUtil,
	private val sessionManager: WebSocketSessionManager,
	private val tokenVerifier: TokenVerifier,
	private val channelUserRepository: ChannelUserRepository,
	private val channelDetailService: ChannelDetailService,
) : TextWebSocketHandler() {

	// 소켓 연결 확인
	override fun afterConnectionEstablished(session: WebSocketSession) {
		// 1. 토큰 검증 단계
		val token = messageUtil.getQueryParams(session)["token"]?: throw BaniException(ErrorType.UNAUTHORIZED)
		val authUserInfo = tokenVerifier.createAuthUserInfo(token)

		val channelId = messageUtil.getChannelId(session)
		val userId = authUserInfo.uid
		logger.info {"userId: ${userId}, channelId: $channelId" }

		// 2. 채널에 해당 유저가 속해있는지 확인하는 단계
		channelUserRepository.findByIdChannelIdAndUserId(channelId, userId)?: throw BaniException(ErrorType.USER_NOT_FOUND)

		// 3. 채팅 상세에 연결
		sessionManager.putChannel(userId, session)

		// 4. TODO 채팅 목록에게도 관련 정보를 보내야 함. (그쪽 세션에도 연결)

		super.afterConnectionEstablished(session)
	}

	// 소켓 통신 시, 메시지의 전송을 다루는 부분
	override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		val channelId = messageUtil.getChannelId(session)

		val msgResult = messageUtil.messageParser(message.payload)

		// TODO : 누구한테 보낼껀지도 정해야지!!!
		when (msgResult.command) {
			"RECENT_MESSAGE" -> channelDetailService.getRecentMessage(channelId, session, msgResult.headers) // 메시지 이력 찾기 (무한 스크롤)
			"SEND_TEXT" -> channelDetailService.sendText(channelId, msgResult.body) // 메시지 전송
		}

	}

	// 소켓 종료 확인
	override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
		sessionManager.deleteChannel(session)
		super.afterConnectionClosed(session, status)
	}

}
