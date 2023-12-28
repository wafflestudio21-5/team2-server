package com.wafflestudio.team2server.common.user

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

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
		// TODO: username을 통해 DB에서 조회
		val user = User("test", encoder.encode("test"), listOf("USER", "ADMIN"))
		return User.builder()
			.username(user.username)
			.password(user.password)
			.roles(*user.roles.toTypedArray())
			.build()
	}

}
