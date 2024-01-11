package com.wafflestudio.team2server.user.model

import com.wafflestudio.team2server.area.model.Area
import java.time.Instant

data class User(
	val id: Long? = null,
	val email: String?,
	val provider: AuthProvider,
	val sub: String?,
	val role: Role,
	val profileImageUrl: String?,
	val nickname: String,
	val mannerTemp: Double,
	val createdAt: Instant?,
	val refAreaIds: List<Area>,
) {

	enum class Role {
		USER, ADMIN
	}

}
