package com.wafflestudio.team2server.channel.repository

import com.wafflestudio.team2server.user.repository.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ChannelUserRepository: JpaRepository<ChannelUserEntity, ChannelUserId> {

	@Query("""
		SELECT cu FROM channel_user cu
		JOIN FETCH cu.channel c
		JOIN FETCH c.productPost p
		WHERE cu.user.id = :userId
	""")
	fun findAllByUserIdWithJoinFetch(userId: Long): List<ChannelUserEntity>

	@Query("""
		SELECT u FROM user u
		JOIN FETCH u.channelUsers cu
		JOIN FETCH cu.channel c
		WHERE c.id IN :channelIds AND u.id != :myUserId
	""")
	fun findUserInfosByChannelId(channelIds: Set<Long>, myUserId: Long): List<UserEntity>

}
