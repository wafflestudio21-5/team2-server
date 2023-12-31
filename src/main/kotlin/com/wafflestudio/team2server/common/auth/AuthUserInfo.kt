package com.wafflestudio.team2server.common.auth

class AuthUserInfo(
	val name: String?,
	private val rawRefAreaIds: List<String>?,
	val issuedAt: Long?,
	val expiredAt: Long?,
) {

	val refAreaIds: List<Int>
		get() = rawRefAreaIds?.mapNotNull(String::toIntOrNull) ?: emptyList()

	val isAdmin: Boolean
		get() = rawRefAreaIds?.any { it == "ADMIN" } ?: false

}
