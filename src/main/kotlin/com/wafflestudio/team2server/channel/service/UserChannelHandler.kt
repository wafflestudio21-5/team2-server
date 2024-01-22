package com.wafflestudio.team2server.channel.service

import com.wafflestudio.team2server.common.auth.TokenVerifier
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.common.util.MessageUtil
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class UserChannelHandler(
	private val sessionManager: WebSocketSessionManager,
	private val messageUtil: MessageUtil,
	private val tokenVerifier: TokenVerifier,
) : TextWebSocketHandler() {

	override fun afterConnectionEstablished(session: WebSocketSession) {
		// 1. 토큰 검증 단계
		val token = messageUtil.getQueryParams(session)["token"]?: throw BaniException(ErrorType.UNAUTHORIZED)
		val authUserInfo = tokenVerifier.verifyJWT(token)
		val userId = authUserInfo.uid

		// 2. 채팅 목록 웹소켓에 연결
		sessionManager.addUserSession(userId, session)

		super.afterConnectionEstablished(session)
	}

	// 목록 화면에서 무언가를 보낼 일은 없지.
	override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		super.handleTextMessage(session, message)
	}

	// 소켓 종료 확인
	override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
		sessionManager.deleteUserSession(session)
		super.afterConnectionClosed(session, status)
	}
}
