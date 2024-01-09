package com.wafflestudio.team2server.post.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductPostRepository : JpaRepository<ProductPostEntity, Long> {
	@Query("select p from product_post p where p.title like '%:title%' and p.sellingArea.id in (:adjAreaIdList)")
	fun findByTitleAndSellingArea(title: String, adjAreaIdList: List<Int>): List<ProductPostEntity>

	@Query(
		"select /*+no_merge(t)*/ * from product_post "
			+ "join (select product_post.id, floor(100*rand(:seed))+id as end from product_post "
			+ "where hidden_yn=0 and product_post.selling_area_id in (:adjAreaIdList)) as t "
			+ "on product_post.id=t.id where end<:cur order by end desc limit 16",
		nativeQuery = true
	)
	fun findRandom(cur: Long, seed: Int, adjAreaIdList: List<Int>): List<PostListSummary>
}
