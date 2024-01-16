package com.wafflestudio.team2server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
@Profile("prod")
class RedisConfig {

	@Bean
	fun redisConnectionFactory(): RedisConnectionFactory {
		val config = RedisStandaloneConfiguration("43.202.236.170", 6379)
		return JedisConnectionFactory(config)
	}

	@Bean
	fun stringRedisTemplate(redisConnectionFactory: RedisConnectionFactory): StringRedisTemplate {
		val template = StringRedisTemplate(redisConnectionFactory)
		template.connectionFactory = redisConnectionFactory
		return template
	}

}
