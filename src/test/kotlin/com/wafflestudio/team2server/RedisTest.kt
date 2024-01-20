package com.wafflestudio.team2server

import com.wafflestudio.team2server.post.repository.AuctionRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RedisTest @Autowired constructor(
	val auctionRepository: AuctionRepository,
) {

	@Test
	fun `RedisTemplate 동작 확인`() {
		auctionRepository.bid(-1L, 1000L, 300.0)
	}

}
