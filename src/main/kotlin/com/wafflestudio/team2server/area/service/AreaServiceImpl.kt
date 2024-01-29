package com.wafflestudio.team2server.area.service

import com.wafflestudio.team2server.area.repository.AreaAdjRepository
import com.wafflestudio.team2server.area.repository.AreaEntity
import com.wafflestudio.team2server.area.repository.AreaRepository
import com.wafflestudio.team2server.common.error.AreaNotFoundException
import com.wafflestudio.team2server.external.KakaoApi
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class AreaServiceImpl(
	private val areaRepository: AreaRepository,
	private val areaAdjRepository: AreaAdjRepository,
	private val kakaoApi: KakaoApi,
) : AreaService {
	@Cacheable("adjarea")
	override fun getAdjAreas(id: Int, distance: Int): List<Int> {
		return listOf(id) + areaAdjRepository.getAreaAdjEntityByAreaIdAndDistanceIsLessThanEqual(id, distance)
			.map { it -> it.targetId }
	}

	@Cacheable("areaentity")
	override fun getAreaById(id: Int): AreaEntity {
		return areaRepository.findById(id).get()
	}

	@Cacheable("areaentity")
	override fun getAreaByCode(code: String): AreaEntity {
		val area = areaRepository.findByCode(code) ?: throw AreaNotFoundException
		return area
	}

	@Cacheable("searcharea")
	override fun searchArea(query: String, cursor: Int): SearchAreaResponse {
		val searchAddress = kakaoApi.searchAddress(query, cursor)
		val hCodes = searchAddress.documents.filter { it.address.isHArea() }
			.map { it.address.hCode }
		val areas = areaRepository.findByCodeIn(hCodes)
		return SearchAreaResponse(
			Meta(searchAddress.meta.isEnd, cursor, areas.size),
			areas.map { Area(it.id, it.code, it.name, it.fullName) }
		)
	}

}
