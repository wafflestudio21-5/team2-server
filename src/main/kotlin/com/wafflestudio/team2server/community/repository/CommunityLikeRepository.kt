package com.wafflestudio.team2server.community.repository

import org.springframework.data.jpa.repository.JpaRepository

interface CommunityLikeRepository : JpaRepository<CommunityLikeEntity, Long> {
	fun existsByUserIdAndCommunityId(userId: Long, communityId: Long): Boolean
	fun findByUserIdAndCommunityId(userId: Long, communityId: Long): CommunityLikeEntity
}
