package com.wafflestudio.team2server.area.service

import com.wafflestudio.team2server.area.model.AreaEntity
import com.wafflestudio.team2server.area.repository.AreaAdjRepository
import com.wafflestudio.team2server.area.repository.AreaRepository
import org.springframework.stereotype.Service

@Service
class AreaServiceImpl(
	private val areaRepository: AreaRepository,
	private val areaAdjRepository: AreaAdjRepository
) {
	fun getAdjAreas(id: Long, distance: Int): Set<Long> {
		return areaAdjRepository.getAreaAdjEntityByAreaIdAndDistanceIsLessThanEqual(id, distance)
			.map { it -> it.targetId }
			.toSet()
	}

	fun getAreaById(id: Long): AreaEntity {
		return areaRepository.findById(id).get()
	}
}
