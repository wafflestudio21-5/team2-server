package com.wafflestudio.team2server.channel.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ChannelRepository : JpaRepository<ChannelEntity, Long> {

//	fun findAllByUserIdWithJoinFetch(userId: Long): List<ChannelEntity>
}
