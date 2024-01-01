package com.wafflestudio.team2server.user.model

data class User(
	val id: Long? = null,
	val email: String,
	val password: String,
	val role: Role,
	val refAreaIds: List<Int>,
) {

	enum class Role {
		USER, ADMIN
	}

}
