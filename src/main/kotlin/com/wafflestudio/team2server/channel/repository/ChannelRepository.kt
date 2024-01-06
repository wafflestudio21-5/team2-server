package com.wafflestudio.team2server.channel.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChannelRepository : JpaRepository<ChannelEntity, Long> {

	@Query("""
		SELECT c FROM channel c
		JOIN c.productPost p
		JOIN c.channelUsers cu
		JOIN cu.user u
		WHERE p.id = :postId
		AND u.id = :userId
	""")
	fun findChannelByPostIdAndUserId(postId: Long, userId: Long): Optional<ChannelEntity>

}
