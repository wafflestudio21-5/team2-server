package com.wafflestudio.team2server.common.auth

import com.wafflestudio.team2server.common.model.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.security.core.userdetails.User as SecurityUser

/**
 * DB에서 유저 정보를 불러오는 서비스.
 */
@Component
class DatabaseUserDetailsService(
	private val encoder: PasswordEncoder,
) : UserDetailsService {

	override fun loadUserByUsername(username: String?): UserDetails {
		if (username == null) {
			throw IllegalArgumentException() // TODO: 예외 처리
		}
		val user = User(
			username = "test",
			password = encoder.encode("test"),
			referenceAreaIds = listOf(1, 2)
		)
		return SecurityUser.builder()
			.username(user.username)
			.password(user.password)
			.roles(*user.referenceAreaIds.map(Int::toString).toTypedArray())
			.build()
	}

}
