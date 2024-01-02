package com.wafflestudio.team2server.post.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
interface ProductPostRepository: JpaRepository<ProductPostEntity, Long> {
	fun findByTitleContaining(title: String): List<ProductPostEntity>
}
