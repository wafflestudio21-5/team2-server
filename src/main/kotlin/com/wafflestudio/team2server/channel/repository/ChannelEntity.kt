package com.wafflestudio.team2server.channel.repository

import com.wafflestudio.team2server.common.util.BaseCreatedDateEntity
import com.wafflestudio.team2server.post.repository.ProductPostEntity
import jakarta.persistence.*
import org.springframework.data.domain.Persistable
import java.time.Instant

@Entity(name = "channel")
class ChannelEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0L,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	val productPost: ProductPostEntity,
	var lastMsg: String? = null,
	var msgUpdatedAt: Instant = Instant.now(),

	@OneToMany(mappedBy = "channel")
	val channelUsers: List<ChannelUserEntity> = mutableListOf(),

	@OneToMany(mappedBy = "channel")
	val channelMessages: List<ChannelMessageEntity> = mutableListOf(),

	) : Persistable<Long>, BaseCreatedDateEntity() {

	override fun getId(): Long {
		return id
	}

	override fun isNew(): Boolean {
		return id == 0L
	}

}
