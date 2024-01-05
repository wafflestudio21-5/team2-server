package com.wafflestudio.team2server.channel.service

import com.wafflestudio.team2server.channel.repository.ChannelUserId
import com.wafflestudio.team2server.channel.repository.ChannelUserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ChannelServiceTest @Autowired constructor(
	private val channelService: ChannelService,
	private val channelUserRepository: ChannelUserRepository,
){
	val userId = 1L
	val newUserId = 6L


	@Test
	fun `쿼리문 확인`() {
		println(channelService.getList(userId))
	}


	@Test
	fun `채팅방 생성`() {
		// given & when
		val channelId = channelService.createChannel(newUserId, 2000L).channelId

		// then
		val channelUsers = channelUserRepository.findAllByIdChannelId(channelId)
		println(channelUsers.size)
		println(channelService.getList(newUserId))
	}

	@Test
	fun `채팅방 상단 고정과 해제`() {
		println(channelService.pin(userId, 1L))
		println(channelUserRepository.findById(ChannelUserId(1L, 1L)).get().pinnedAt)
		println(channelService.getList(userId))
		println(channelService.unpin(userId, 1L))
		println(channelService.getList(userId))
	}

}
