package com.wafflestudio.team2server.area.controller

import com.wafflestudio.team2server.area.service.AreaService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AreaController(private val areaService: AreaService) {
	@GetMapping("/area")
	fun getAreaInfo(code: Int) {

	}
}
