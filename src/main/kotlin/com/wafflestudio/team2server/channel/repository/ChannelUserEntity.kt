package com.wafflestudio.team2server.channel.repository

import com.wafflestudio.team2server.common.util.BaseCreatedDateEntity
import com.wafflestudio.team2server.user.repository.UserEntity
import jakarta.persistence.*
import org.springframework.data.domain.Persistable
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "channel_user")
class ChannelUserEntity(

	@EmbeddedId
	private val id: ChannelUserId,

	@MapsId("channelId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	val channel: ChannelEntity,

	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	val user: UserEntity,

	var pinnedAt: LocalDateTime? = null,
	) : Persistable<ChannelUserId>, BaseCreatedDateEntity() {

	override fun getId(): ChannelUserId {
		return id
	}

	override fun isNew(): Boolean {
		return createdAt == null
	}

}

@Embeddable
data class ChannelUserId(
	var channelId: Long = 0L,
	var userId: Long = 0L,
) : Serializable
