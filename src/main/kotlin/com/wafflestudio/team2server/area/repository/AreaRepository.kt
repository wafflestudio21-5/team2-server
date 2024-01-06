package com.wafflestudio.team2server.area.repository

import com.wafflestudio.team2server.area.model.AreaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AreaRepository : JpaRepository<AreaEntity, Int> {
}
