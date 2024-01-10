package com.wafflestudio.team2server.area.repository

import com.wafflestudio.team2server.user.repository.UserEntity
import jakarta.persistence.*
import org.springframework.data.domain.Persistable
import java.io.Serializable
import java.time.LocalDateTime

@Entity(name = "active_area")
class AreaUserEntity(

	@EmbeddedId
	private val id: AreaUserId,

	@MapsId("areaId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reference_area_id")
	val area: AreaEntity,

	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	val user: UserEntity,

	@JoinColumn(name = "authenticated_at")
	var authenticatedAt: LocalDateTime? = null,

	val count: Int
) : Persistable<AreaUserId> {
	override fun getId(): AreaUserId {
		return id
	}

	override fun isNew(): Boolean {
		return authenticatedAt == null
	}

	@PrePersist
	fun touchForSave() {
		authenticatedAt = LocalDateTime.now()
	}
}

@Embeddable
data class AreaUserId(
	val areaId: Int = 0,
	val userId: Long = 0L,
) : Serializable
