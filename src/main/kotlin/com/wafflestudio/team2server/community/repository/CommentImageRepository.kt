package com.wafflestudio.team2server.community.repository

import com.wafflestudio.team2server.community.repos.CommentImageEntity
import com.wafflestudio.team2server.community.repos.CommunityImageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommentImageRepository : JpaRepository<CommentImageEntity, Long> {
	fun findByCommentId(id: Long): List<CommentImageEntity>
}
