package com.wafflestudio.team2server.channel.service

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import java.util.TimerTask

private val logger: KLogger = KotlinLogging.logger {}

class PingTimerTask(
	private val webSocketSession: WebSocketSession,
) : TimerTask() {
	override fun run() {
		logger.info { "[ping-error] session expired: session=$webSocketSession" }
		webSocketSession.close(CloseStatus.POLICY_VIOLATION)
	}
}
