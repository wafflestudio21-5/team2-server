package com.wafflestudio.team2server.area.service

import com.wafflestudio.team2server.area.model.AreaEntity
import org.springframework.stereotype.Service


@Service
interface AreaService {
	fun getAdjAreas(id: Int, distance: Int): List<Int>
	fun getAreaById(id: Int): AreaEntity
	fun getAreaByCode(code: String): AreaEntity
	fun searchArea(query: String, cursor: Int)
}
