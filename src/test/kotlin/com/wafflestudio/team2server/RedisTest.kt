package com.wafflestudio.team2server

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate

@SpringBootTest
class RedisTest @Autowired constructor(
	val redisTemplate: StringRedisTemplate,
) {

	@Test
	fun `RedisTemplate 동작 확인`() {
		redisTemplate.opsForList().leftPush("test", "hello")
	}

}
