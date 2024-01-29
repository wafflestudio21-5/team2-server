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
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
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
	fun lettuceConnectionFactory(): LettuceConnectionFactory {
		val lettuceClientConfiguration = LettuceClientConfiguration.builder()
			.commandTimeout(Duration.ZERO)
			.shutdownTimeout(Duration.ZERO)
			.build()
		val redisStandaloneConfiguration = RedisStandaloneConfiguration("localhost", 6379)
		return LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration)
	}

	@Bean
	fun redisTemplate(): RedisTemplate<*, *> {
		val template = RedisTemplate<ByteArray, ByteArray>()
		template.keySerializer = StringRedisSerializer()
		template.valueSerializer = StringRedisSerializer()
		template.setConnectionFactory(lettuceConnectionFactory())
		return template
	}

	@Bean
	fun cacheManager(): CacheManager {
		val builder = RedisCacheManager.RedisCacheManagerBuilder
			.fromConnectionFactory(lettuceConnectionFactory())
		val configuration = RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(Duration.ofMinutes(1))
		builder.cacheDefaults(configuration)
		return builder.build()
	}
}
