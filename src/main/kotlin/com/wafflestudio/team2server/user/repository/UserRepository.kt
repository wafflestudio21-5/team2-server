package com.wafflestudio.team2server.user.repository

import com.wafflestudio.team2server.user.model.AuthProvider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, Long> {

	@Query("""
		SELECT u from user u
		JOIN FETCH u.areaUsers au
		JOIN FETCH au.area a
		WHERE u.email = :email
	""")
	fun findByEmailWithJoinFetch(email: String): UserEntity?

	fun findByEmail(email: String): UserEntity?

	fun existsByEmail(email: String): Boolean

	fun existsByNickname(nickname: String): Boolean

	fun existsByProviderAndSub(provider: AuthProvider, sub: String): Boolean

}
