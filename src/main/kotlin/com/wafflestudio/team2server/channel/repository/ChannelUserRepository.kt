package com.wafflestudio.team2server.channel.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ChannelUserRepository: JpaRepository<ChannelUserEntity, ChannelUserId> {

	@Query("""
		SELECT cu FROM channel_user cu
		JOIN FETCH cu.channel c
		JOIN cu.user u
		WHERE u.id = :userId
	""")
	fun findChannelsByUserId(userId: Long): List<ChannelUserEntity>

	@Query("""
		SELECT cu FROM channel_user cu
		JOIN FETCH cu.user u
		JOIN FETCH cu.channel c
		JOIN FETCH c.productPost p
		JOIN FETCH p.sellingArea a
		WHERE c.id IN :channelIds AND u.id != :myUserId
	""")
	fun findChannelInfosByChannelIds(channelIds: Set<Long>, myUserId: Long): List<ChannelUserEntity>

	@Query("""
		SELECT cu FROM channel_user cu
		JOIN FETCH cu.user u
		JOIN FETCH cu.channel c
		JOIN FETCH c.productPost p
		WHERE c.id = :channelId AND u.id != :myUserId
	""")
	fun findChannelInfo(channelId: Long, myUserId: Long): ChannelUserEntity


	fun findAllByIdChannelId(channelId: Long): Set<ChannelUserEntity>

	fun findByIdChannelIdAndUserId(channelId: Long, userId: Long): ChannelUserEntity?

}
