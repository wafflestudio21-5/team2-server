package com.wafflestudio.team2server.common.user

data class User(
	val username: String,
	val password: String,
	val roles: List<String>,
)
