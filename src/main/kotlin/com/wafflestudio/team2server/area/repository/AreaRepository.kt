package com.wafflestudio.team2server.area.repository

import org.springframework.data.jpa.repository.JpaRepository


interface AreaRepository: JpaRepository<AreaEntity, Int> {
	fun findByCode(code: String): AreaEntity?
	fun findByCodeIn(code: List<String>): List<AreaEntity>
}
