package com.wafflestudio.team2server.area.service

import com.wafflestudio.team2server.area.model.AreaEntity
import com.wafflestudio.team2server.area.repository.AreaAdjRepository
import com.wafflestudio.team2server.area.repository.AreaRepository
import org.springframework.stereotype.Service

@Service
class AreaServiceImpl(
	private val areaRepository: AreaRepository,
	private val areaAdjRepository: AreaAdjRepository
) : AreaService {
	override fun getAdjAreas(id: Int, distance: Int): List<Int> {
		return listOf(id) + areaAdjRepository.getAreaAdjEntityByAreaIdAndDistanceIsLessThanEqual(id, distance)
			.map { it -> it.targetId }
	}

	override fun getAreaById(id: Int): AreaEntity {
		return areaRepository.findById(id).get()
	}
}
