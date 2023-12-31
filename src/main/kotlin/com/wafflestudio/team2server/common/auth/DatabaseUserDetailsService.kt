package com.wafflestudio.team2server.common.auth

import com.wafflestudio.team2server.common.model.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
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
		// TODO: DB에서 조회하도록 바꾸어야함.
		val user = User(
			username = "test",
			password = encoder.encode("test"),
			role = User.Role.ADMIN,
			refAreaIds = listOf(1, 2),
		)
		return SecurityUser.builder()
			.username(user.username)
			.password(user.password)
			.authorities(user.refAreaIds.map { SimpleGrantedAuthority(it.toString()) } + SimpleGrantedAuthority(user.role.name))
			.build()
	}

}
