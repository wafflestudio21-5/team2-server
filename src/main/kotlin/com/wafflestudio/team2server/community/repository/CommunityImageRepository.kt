package com.wafflestudio.team2server.community.repository

import com.wafflestudio.team2server.community.repos.CommunityImageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommunityImageRepository : JpaRepository<CommunityImageEntity, Long> {
	fun findByCommunityId(id: Long): List<CommunityImageEntity>
}
