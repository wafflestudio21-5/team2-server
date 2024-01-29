package com.wafflestudio.team2server.user.repository

import com.wafflestudio.team2server.user.model.AuthProvider
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, Long> {

	@Query(
		"""
		SELECT u from user u
		LEFT JOIN FETCH u.areaUsers au
		LEFT JOIN FETCH au.area a
		WHERE u.email = :email
	"""
	)
	@Cacheable("userentity")
	fun findByEmailWithJoinFetch(email: String): UserEntity?

	@Query(
		"""
		SELECT u from user u
		LEFT JOIN FETCH u.areaUsers au
		LEFT JOIN FETCH au.area a
		WHERE u.id = :id
	"""
	)
	@Cacheable("userentity")
	fun findByIdWithJoinFetch(id: Long): UserEntity?

	@Query(
		"""
		SELECT u from user u
		LEFT JOIN FETCH u.areaUsers au
		LEFT JOIN FETCH au.area a
		WHERE u.provider = :provider AND u.sub = :sub
	"""
	)
	@Cacheable("userentity")
	fun findByProviderAndSubWithJoinFetch(provider: AuthProvider, sub: String): UserEntity?

	fun existsByEmail(email: String): Boolean

	fun existsByNickname(nickname: String): Boolean

	fun existsByProviderAndSub(provider: AuthProvider, sub: String): Boolean

}
