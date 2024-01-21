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

	// 채팅 상세 화면에 지금 연결되어 있는 세션들을 userId에 대응하여 저장
	private val userChannelSession = ConcurrentHashMap<Long, WebSocketSession>()
	// 채팅 목록 화면에 지금 연결되어 있는 세션들을 userId에 대응하여 저장
	private val userSession = ConcurrentHashMap<Long, WebSocketSession>()
	// 각 채팅에 속해있는 userId 들을 저장
	private val channelUserMap = ConcurrentHashMap<Long, CopyOnWriteArraySet<Long>>()
	// session 을 통해 userId를 알아보기 위한 map (채팅 상세 화면)
	private val sessionUserMapForChannel = ConcurrentHashMap<WebSocketSession, Long>()
	// session 을 통해 userId를 알아보기 위한 map (채팅 목록 화면)
	private val sessionUserMapForUser = ConcurrentHashMap<WebSocketSession, Long>()

	/**
	 * 특정 채널에 속해있는 사용자들의 session을 저장
	 */
	fun addChannelSession(userId: Long, session: WebSocketSession, channelUsers: Set<Long>) {
		val channelId = messageUtil.getChannelId(session)
		if (channelUserMap[channelId] == null) {
			val users = CopyOnWriteArraySet(channelUsers)
			channelUserMap[channelId] = users
		}
		userChannelSession[userId] = session
		sessionUserMapForChannel[session] = userId
	}

	fun deleteChannelSession(session: WebSocketSession) {
		val userId = sessionUserMapForChannel[session]
		sessionUserMapForChannel.remove(session)
		userChannelSession.remove(userId)
	}

	fun findChannelUsers(channelId: Long): Set<Long> {
		return channelUserMap[channelId]?: throw BaniException(ErrorType.INVALID_CHANNEL_ID)
	}

	fun findUserIdFromChannelSession(session: WebSocketSession): Long {
		return sessionUserMapForChannel[session]?: throw BaniException(ErrorType.INVALID_SESSION)
	}

	fun findSessionFromUserId(userId: Long): WebSocketSession {
		return userSession[userId]?: throw BaniException(ErrorType.INVALID_USER_ID)
	}

	fun findDetailSessionFromUserId(userId: Long): WebSocketSession {
		return userChannelSession[userId]?: throw BaniException(ErrorType.INVALID_USER_ID)
	}

	fun addUserSession(userId: Long, session: WebSocketSession) {
		userSession[userId] = session
		sessionUserMapForUser[session] = userId
	}

	fun deleteUserSession(session: WebSocketSession) {
		val userId = sessionUserMapForUser[session]
		userSession.remove(userId)
		sessionUserMapForUser.remove(session)
	}
}
