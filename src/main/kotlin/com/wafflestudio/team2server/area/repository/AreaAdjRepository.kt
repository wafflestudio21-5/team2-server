package com.wafflestudio.team2server.area.repository

import org.springframework.data.jpa.repository.JpaRepository

interface AreaAdjRepository : JpaRepository<AreaAdjEntity, Int> {
	fun getAreaAdjEntityByAreaIdAndDistanceIsLessThanEqual(id: Int, distance: Int): List<AreaAdjEntity>
}
