package com.wafflestudio.team2server.area.repository

import org.springframework.data.jpa.repository.JpaRepository

interface AreaUserRepository: JpaRepository<AreaUserEntity, AreaUserId> {
}
