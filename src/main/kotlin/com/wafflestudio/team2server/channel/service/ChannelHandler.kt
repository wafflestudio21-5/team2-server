package com.wafflestudio.team2server.channel.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler


private val logger: KLogger = KotlinLogging.logger { }

@Component
class ChannelHandler(
	private val mapper: ObjectMapper,
) : TextWebSocketHandler() {

	// 소켓 연결 확인
	override fun afterConnectionEstablished(session: WebSocketSession) {
		super.afterConnectionEstablished(session)
	}

	// 소켓 통신 시, 메시지의 전송을 다루는 부분
	override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		val payload = message.payload
		logger.info {"web socket payload: $payload" }
	}

	// 소켓 종료 확인
	override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
		super.afterConnectionClosed(session, status)
	}




}
