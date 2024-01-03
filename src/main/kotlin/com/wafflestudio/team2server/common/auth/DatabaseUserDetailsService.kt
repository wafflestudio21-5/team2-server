package com.wafflestudio.team2server.common.auth

import com.wafflestudio.team2server.user.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.security.core.userdetails.User as SecurityUser

/**
 * DB에서 유저 정보를 불러오는 서비스.
 */
@Component
class DatabaseUserDetailsService(
	private val userRepository: UserRepository,
) : UserDetailsService {

	override fun loadUserByUsername(email: String?): UserDetails {
		if (email == null) {
			throw IllegalArgumentException() // TODO: 예외 처리
		}
		val user = userRepository.findByEmail(email) ?: throw RuntimeException("user not found")
		return SecurityUser.builder()
			.username(user.id.toString())
			.password(user.password)
			//.authorities(user.refAreaIds.map { SimpleGrantedAuthority(it.toString()) } + SimpleGrantedAuthority(user.role.name))
			.authorities(emptyList<Int>().map { SimpleGrantedAuthority(it.toString()) } + SimpleGrantedAuthority(user.role.name))
			.build()
	}

}
