package com.wafflestudio.team2server.common.model

data class User(
	val id: Long? = null,
	val username: String,
	val password: String,
	val referenceAreaIds: List<Int>, // 지연 번호가 ROLE 역할
)
