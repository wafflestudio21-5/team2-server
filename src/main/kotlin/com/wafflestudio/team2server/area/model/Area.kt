package com.wafflestudio.team2server.area.model

import java.time.LocalDateTime

data class Area(
	val id: Int,
	val code: String,
	val fullName: String,
	val name: String,
	val sggName: String,
	val sdName: String,
	val authenticatedAt: LocalDateTime?,
	val count: Int,
)
