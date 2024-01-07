package com.wafflestudio.team2server.channel.repository

import com.wafflestudio.team2server.user.repository.UserEntity
import com.wafflestudio.team2server.common.util.BaseCreatedDateEntity
import jakarta.persistence.*

@Entity(name = "channel_message")
class ChannelMessageEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0L,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	val channel: ChannelEntity,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	val sender: UserEntity,

	val message: String,
	var readYn: Boolean = false,
	val msgNo: Long = 0L,

	):BaseCreatedDateEntity() {
}
