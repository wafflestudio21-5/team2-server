package com.wafflestudio.team2server.area.repository

import com.wafflestudio.team2server.area.model.AreaUserEntity
import com.wafflestudio.team2server.area.model.AreaUserId
import org.springframework.data.jpa.repository.JpaRepository

interface AreaUserRepository: JpaRepository<AreaUserEntity, AreaUserId> {
}
