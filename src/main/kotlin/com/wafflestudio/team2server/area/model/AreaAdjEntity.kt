package com.wafflestudio.team2server.area.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity(name = "area_adj")
class AreaAdjEntity(
	@Id
	val id: Long,
	val areaId: Long,
	val targetId: Long,
	val distance: Int
)
