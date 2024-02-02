package com.wafflestudio.team2server.post.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductPostRepository : JpaRepository<ProductPostEntity, Long> {
	@Query(
		"""select p from product_post p
			where (upper(p.title) like concat('%',upper(:keyword),'%')
			or upper(p.description) like concat('%',upper(:keyword),'%'))
			and (p.sellingArea.id in (:adjAreaIdList) and p.id<:cur and p.hiddenYn=false)
			order by p.id desc limit 16"""
	)
	fun findByKeywordIgnoreCaseAndSellingArea(cur: Long, keyword: String, adjAreaIdList: List<Int>): List<ProductPostEntity>

	@Query(
		"""select /*+no_merge(t)*/ * from product_post
			join (select product_post.id, floor(1000*rand(:seed))+id
			as end from product_post
			where hidden_yn=0 and product_post.selling_area_id in (:adjAreaIdList)
			order by product_post.id desc limit :start, 300) as t
			on product_post.id=t.id where end<:cur order by end desc limit 16""",
		nativeQuery = true
	)
	fun findRandom(cur: Long, seed: Int, adjAreaIdList: List<Int>, start: Int): List<PostListSummary>
}
