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
		val authUserInfo = tokenVerifier.verifyJWT(token)

		val channelId = messageUtil.getChannelId(session)
		val userId = authUserInfo.uid
		logger.info {"userId: ${userId}, channelId: $channelId" }

		// 2. 채널에 해당 유저가 속해있는지 확인하는 단계
		val channelUsers = channelUserRepository.findAllByIdChannelId(channelId).map { it.id.userId }.toSet()
		if (!channelUsers.contains(userId)) {
			throw BaniException(ErrorType.USER_NOT_FOUND)
		}

		// 3. 채팅 상세에 연결
		sessionManager.addChannelSession(userId, session, channelUsers)

		super.afterConnectionEstablished(session)
	}

	// 소켓 통신 시, 메시지의 전송을 다루는 부분
	override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		val channelId = messageUtil.getChannelId(session)

		val msgResult = messageUtil.messageParser(message.payload)

		when (msgResult.command) {
			"RECENT_MESSAGE" -> { // 메시지 이력 찾기 (무한 스크롤)
				sessionManager.refreshPingTimer(session)
				channelDetailService.getRecentMessage(channelId, session, msgResult.headers)
			}
			"SEND_TEXT" -> { // 메시지 전송
				sessionManager.refreshPingTimer(session)
				channelDetailService.sendText(channelId, session, msgResult.body)
			}
			"PING" -> sessionManager.refreshPingTimer(session) // 핑 초기화
			"DISCONNECT" -> session.close(CloseStatus.NORMAL)
		}

	}

	// 소켓 종료 확인
	override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
		logger.info { "ws close: status=$status" }
		sessionManager.deleteChannelSession(session)
		super.afterConnectionClosed(session, status)
	}

}
