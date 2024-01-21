package com.wafflestudio.team2server.channel.repository

import jakarta.persistence.*

@Entity(name = "message_sequence")
class MessageSequenceEntity(
	@Id
	@Column(name = "channel_id")
	val id: Long = 0L,

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "channel_id")
	val channel: ChannelEntity,
	var nextMsgNo: Long = 0L,

)
