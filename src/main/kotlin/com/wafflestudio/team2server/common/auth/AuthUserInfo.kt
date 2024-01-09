package com.wafflestudio.team2server.common.auth

class AuthUserInfo(
	val uid: Long,
	val refAreaIds: List<Int>,
	val isAdmin: Boolean,
	val issuedAt: Long?,
	val expiredAt: Long?,
)
