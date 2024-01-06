package com.wafflestudio.team2server.channel.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class ChannelUserRepositoryTest @Autowired constructor(
	private val channelUserRepository: ChannelUserRepository

){
	@Test
	fun `쿼리 확인`() {
		println(channelUserRepository.findAllByIdChannelId(1L))
	}
}
