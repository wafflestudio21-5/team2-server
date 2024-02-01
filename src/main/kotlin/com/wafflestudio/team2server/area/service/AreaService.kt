package com.wafflestudio.team2server.area.service

import com.wafflestudio.team2server.area.repository.AreaEntity
import org.springframework.stereotype.Service
import java.io.Serializable


@Service
interface AreaService {
	fun getAdjAreas(id: Int, distance: Int): List<Int>
	fun getAreaById(id: Int): AreaEntity
	fun getAreaByCode(code: String): AreaEntity
	fun searchArea(query: String, cursor: Int): SearchAreaResponse
}

data class SearchAreaResponse(
	val meta: Meta,
	val areas: List<Area>
) : Serializable

data class Meta(
	val isEnd: Boolean,
	val cursor: Int,
	val totalCount: Int,
) : Serializable

data class Area(
	val id: Int,
	val code: String,
	val name: String,
	val fullName: String,
) : Serializable
