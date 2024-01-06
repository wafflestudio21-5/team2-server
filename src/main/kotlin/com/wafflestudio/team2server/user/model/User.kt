package com.wafflestudio.team2server.user.model

import java.time.LocalDateTime

data class User(
	val id: Long? = null,
	val email: String?,
	val provider: AuthProvider,
	val sub: String?,
	val role: Role,
	val profileImageUrl: String?,
	val nickname: String,
	val mannerTemp: Double,
	val createdAt: LocalDateTime,
	val refAreaIds: List<Int>,
) {

	enum class Role {
		USER, ADMIN
	}

}
