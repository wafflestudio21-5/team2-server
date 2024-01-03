package com.wafflestudio.team2server.user.repository

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {

	fun findByEmail(email: String): UserEntity?

}
