package com.wafflestudio.team2server.area.service

import org.springframework.stereotype.Service


@Service
interface AreaService {
	fun getAdjAreas(id: Long, distance: Int): Set<Long>
}
