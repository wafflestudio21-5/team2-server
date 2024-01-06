package com.wafflestudio.team2server.area.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity(name = "area_adj")
class AreaAdjEntity(
	@Id
	val id: Int,
	val areaId: Int,
	val targetId: Int,
	val distance: Int
)
