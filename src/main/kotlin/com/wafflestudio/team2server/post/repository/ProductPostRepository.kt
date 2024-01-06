package com.wafflestudio.team2server.post.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductPostRepository : JpaRepository<ProductPostEntity, Long> {
	@Query("select p from product_post p where p.title like '%:title%' and p.sellingArea.id in (:adjAreaIdList)")
	fun findByTitleAndSellingArea(title: String, adjAreaIdList: List<Int>): List<ProductPostEntity>
}
