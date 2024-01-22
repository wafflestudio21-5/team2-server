package com.wafflestudio.team2server.community.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CommentRepository: JpaRepository<CommentEntity, Long> {
	@Query("""
		select c from community_comment c where c.community.id = :communityId and c.parentId = c.id
	""")
	fun findByCommunityId(communityId: Long): List<CommentEntity>

	fun findByParentId(parentId: Long): List<CommentEntity>
}
