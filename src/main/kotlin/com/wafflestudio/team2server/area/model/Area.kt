package com.wafflestudio.team2server.area.model

data class Area(
	val id: Int,
	val code: String,
	val fullName: String,
	val name: String,
	val sggName: String,
	val sdName: String,
	val authenticatedAt: Long?,
	val count: Int,
)
