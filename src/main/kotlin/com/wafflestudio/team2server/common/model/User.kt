package com.wafflestudio.team2server.common.model

data class User(
	val id: Long? = null,
	val username: String,
	val password: String,
	val role: Role,
	val refAreaIds: List<Int>,
) {

	enum class Role {
		USER, ADMIN
	}

}
