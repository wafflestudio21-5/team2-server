package com.wafflestudio.team2server.area.repository

import com.wafflestudio.team2server.user.repository.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AreaUserRepository: JpaRepository<AreaUserEntity, AreaUserId> {
	fun findByUser(user: UserEntity): List<AreaUserEntity>

	@Query("""DELETE FROM active_area a
		WHERE a.id.userId = :uid AND a.id.areaId = :areaId""")
	fun deleteByUserIdAndAreaId(uid: Long, areaId: Int): Long
}
