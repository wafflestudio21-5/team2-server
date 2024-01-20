package com.wafflestudio.team2server.channel.service

import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import com.wafflestudio.team2server.common.util.MessageUtil
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet


@Component
class WebSocketSessionManager(
	val messageUtil: MessageUtil
) {

	private val channelSessions = ConcurrentHashMap<Long, CopyOnWriteArraySet<WebSocketSession>>()
	private val userSession = ConcurrentHashMap<Long, WebSocketSession>()
	private val sessionUserMap = ConcurrentHashMap<WebSocketSession, Long>()

	fun putChannel(userId: Long, session: WebSocketSession) {
		val channelId = messageUtil.getChannelId(session)
		var webSocketSessions = channelSessions[channelId]
		if (webSocketSessions == null) {
			webSocketSessions = CopyOnWriteArraySet<WebSocketSession>()
		}
		webSocketSessions.add(session)
		channelSessions[channelId] = webSocketSessions
		sessionUserMap[session] = userId
	}

	fun deleteChannel(session: WebSocketSession) {
		val channelId = messageUtil.getChannelId(session)
		val webSocketSessions = channelSessions[channelId]?: throw RuntimeException()
		webSocketSessions -= session
	}

	fun findUserIdFromSession(session: WebSocketSession): Long {
		return sessionUserMap[session]?: throw BaniException(ErrorType.INVALID_SESSION)
	}

}
