package com.wafflestudio.team2server.area.controller

import com.wafflestudio.team2server.area.service.AreaService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AreaController(private val areaService: AreaService) {
	@GetMapping("/area")
	fun getAreaInfo(code: Long) {
	}

	@GetMapping("/area/search")
	fun searchArea(@RequestParam("query") query: String, @RequestParam("cursor", defaultValue = "1") cursor: Int) {
		areaService.searchArea(query, cursor)
	}
}
