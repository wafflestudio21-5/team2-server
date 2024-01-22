package com.wafflestudio.team2server.channel.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ChannelMessageRepository: JpaRepository<ChannelMessageEntity, Long> {
	@Query("""
		SELECT m FROM channel_message m
		JOIN FETCH m.sender
		JOIN m.channel
		WHERE m.channel.id = :channelId
		AND m.msgNo <= :cursor
		ORDER BY m.msgNo DESC
		LIMIT 30
	""")
	fun findRecentMessagesWithCursor(channelId: Long, cursor: Long): List<ChannelMessageEntity>

	@Query("""
		SELECT m FROM channel_message m
		JOIN FETCH m.sender
		JOIN m.channel
		WHERE m.channel.id = :channelId
		ORDER BY m.msgNo DESC
		LIMIT 30
	""")
	fun findRecentMessages(channelId: Long): List<ChannelMessageEntity>

}
