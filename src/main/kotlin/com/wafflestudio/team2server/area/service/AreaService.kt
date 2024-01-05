package com.wafflestudio.team2server.area.service

import com.wafflestudio.team2server.area.model.AreaEntity
import org.springframework.stereotype.Service


@Service
interface AreaService {
	fun getAdjAreas(id: Long, distance: Int): Set<Long>
	fun getAreaById(id: Long): AreaEntity
}
