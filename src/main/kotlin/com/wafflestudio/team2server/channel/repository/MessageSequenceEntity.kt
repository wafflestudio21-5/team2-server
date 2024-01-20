package com.wafflestudio.team2server.channel.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne

@Entity(name = "message_sequence")
class MessageSequenceEntity(
	@Id
	@Column(name = "channel_id")
	val id: Long,
	@OneToOne
	@MapsId
	@JoinColumn(name = "channel_id")
	val channel: ChannelEntity,
	val nextMsgNo: Long = 0L,

)
