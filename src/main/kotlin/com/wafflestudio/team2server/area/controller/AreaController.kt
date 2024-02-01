package com.wafflestudio.team2server.area.controller

import com.wafflestudio.team2server.area.service.AreaService
import com.wafflestudio.team2server.area.service.SearchAreaResponse
import com.wafflestudio.team2server.common.error.BadRequestException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AreaController(private val areaService: AreaService) {
	@GetMapping("/area/search")
	fun searchArea(@RequestParam("query") query: String, @RequestParam("cursor", defaultValue = "1") cursor: Int): SearchAreaResponse {
		if (query.isBlank()) {
			throw BadRequestException
		}
		return areaService.searchArea(query, cursor)
	}
}
