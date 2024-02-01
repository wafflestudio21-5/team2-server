package com.wafflestudio.team2server.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig(
	@Value("\${redis.host}") private val host: String,
	@Value("\${redis.port}") private val port: Int,
	@Value("\${redis.passwd}") private val passwd: String,
) {

	@Bean
	fun redisConnectionFactory(): RedisConnectionFactory {
		val config = RedisStandaloneConfiguration(host, port).apply {
			setPassword(passwd)
		}
		return JedisConnectionFactory(config)
	}

	@Bean
	fun stringRedisTemplate(redisConnectionFactory: RedisConnectionFactory): StringRedisTemplate {
		val template = StringRedisTemplate(redisConnectionFactory)
		template.connectionFactory = redisConnectionFactory
		return template
	}

	@Bean
	fun cacheManager(): CacheManager {
		val builder = RedisCacheManager.RedisCacheManagerBuilder
			.fromConnectionFactory(redisConnectionFactory())
		val configuration = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofMinutes(1))
		builder.cacheDefaults(configuration)
		return builder.build()
	}
}
