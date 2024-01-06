package com.wafflestudio.team2server.user.repository

import com.wafflestudio.team2server.user.model.AuthProvider
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {

	fun findByEmail(email: String): UserEntity?

	fun existsByEmail(email: String): Boolean

	fun existsByNickname(nickname: String): Boolean

	fun existsByProviderAndSub(provider: AuthProvider, sub: String): Boolean

}
