package com.wafflestudio.team2server.area.controller

import com.wafflestudio.team2server.area.service.Area
import com.wafflestudio.team2server.area.service.AreaService
import com.wafflestudio.team2server.area.service.SearchAreaResponse
import com.wafflestudio.team2server.common.error.BadRequestException
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AreaController(private val areaService: AreaService) {
	@GetMapping("/area/{id}")
	@Cacheable("area")
	fun getAreaInfo(@PathVariable id: Int): Area {
		val area = areaService.getAreaById(id)
		return Area(area.id, area.code, area.name, area.fullName)
	}

	@GetMapping("/area/search")
	@Cacheable("searcharea")
	fun searchArea(@RequestParam("query") query: String, @RequestParam("cursor", defaultValue = "1") cursor: Int): SearchAreaResponse {
		if (query.isBlank()) {
			throw BadRequestException
		}
		return areaService.searchArea(query, cursor)
	}
}
