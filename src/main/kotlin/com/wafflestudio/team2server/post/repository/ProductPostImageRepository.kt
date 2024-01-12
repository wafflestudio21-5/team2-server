package com.wafflestudio.team2server.post.repository

import org.springframework.data.jpa.repository.JpaRepository

interface ProductPostImageRepository : JpaRepository<ProductPostImageEntity, Long> {
	fun findByProductPostId(id: Long): List<ProductPostImageEntity>
}
