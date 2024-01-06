package com.wafflestudio.team2server.area.repository

import com.wafflestudio.team2server.area.model.AreaAdjEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AreaAdjRepository : JpaRepository<AreaAdjEntity, Long> {
	fun getAreaAdjEntityByAreaIdAndDistanceIsLessThanEqual(id: Int, distance: Int): List<AreaAdjEntity>
}
