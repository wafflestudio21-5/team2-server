package com.wafflestudio.team2server.common.auth

import com.wafflestudio.team2server.common.error.EmailBlankException
import com.wafflestudio.team2server.common.error.UserNotFoundException
import com.wafflestudio.team2server.user.repository.UserEntity
import com.wafflestudio.team2server.user.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.security.core.userdetails.User as SecurityUser

/**
 * DB에서 유저 정보를 불러 오는 서비스.
 */
@Component
class DatabaseUserDetailsService(
	private val userRepository: UserRepository,
) : UserDetailsService {

	override fun loadUserByUsername(email: String?): UserDetails {
		if (email == null) {
			throw EmailBlankException
		}
		val user = userRepository.findByEmailWithJoinFetch(email) ?: throw UserNotFoundException
		return UserWrapper(
			SecurityUser.builder()
				.username(user.id.toString())
				.password(user.password)
				.authorities(SimpleGrantedAuthority(user.role.name))
				.build(),
			user
		)
	}

	class UserWrapper(
		private val user: UserDetails,
		val userEntity: UserEntity,
	) : UserDetails {
		override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
			return user.authorities
		}

		override fun getPassword(): String {
			return user.password
		}

		override fun getUsername(): String {
			return user.username
		}

		override fun isAccountNonExpired(): Boolean {
			return user.isAccountNonExpired
		}

		override fun isAccountNonLocked(): Boolean {
			return user.isAccountNonLocked
		}

		override fun isCredentialsNonExpired(): Boolean {
			return user.isCredentialsNonExpired
		}

		override fun isEnabled(): Boolean {
			return user.isEnabled
		}

	}

}
