package com.wafflestudio.team2server.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BaseConfig {

	@Bean
	fun jsonMapper(): JsonMapper {
		val kotlinModule = KotlinModule.Builder()
			.build()
		return JsonMapper.builder()
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.addModule(JavaTimeModule())
			.addModule(kotlinModule)
			.build()
	}

}
