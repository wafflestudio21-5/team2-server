package com.wafflestudio.team2server.channel.repository

import com.wafflestudio.team2server.user.repository.UserEntity
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "channel_user")
class ChannelUserEntity(

	@EmbeddedId
	private val id: ChannelUserId,

	@MapsId("channelId")
	@ManyToOne
	@JoinColumn(name = "channel_id")
	val channel: ChannelEntity,

	@MapsId("userId")
	@ManyToOne
	@JoinColumn(name = "user_id")
	val user: UserEntity,

	var pinnedAt: LocalDateTime?,

	)

@Embeddable
data class ChannelUserId(
	var channelId: Long = 0L,
	var userId: Long = 0L,
): Serializable
