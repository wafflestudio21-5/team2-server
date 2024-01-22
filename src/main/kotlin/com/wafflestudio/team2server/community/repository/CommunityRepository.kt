package com.wafflestudio.team2server.community.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CommunityRepository : JpaRepository<CommunityEntity, Long> {
	@Query(
		"""
		select /*+no_merge(t)*/ * from community
		join (
		  select community.id, floor(1000*rand(:seed))+id as end
		  from community
		  where community.areaId in (:adjAreaIdList) limit :start, 300
		) as t
		on community.id = t.id
		where end < :cur order by end desc limit 16
		""",
		nativeQuery = true
	)
	fun findRandom(cur:Long, seed: Int, adjAreaIdList: List<Int>, start: Int): List<CommunityListSummary>
}
