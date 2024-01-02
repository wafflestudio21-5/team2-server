package com.wafflestudio.team2server.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

	@Bean
	fun openAPI():OpenAPI {
		val jwtSchemeName = "jwtAuth"
		val securityRequirement = SecurityRequirement().addList(jwtSchemeName)
		val components = Components()
			.addSecuritySchemes(jwtSchemeName, SecurityScheme()
				.name(jwtSchemeName)
				.type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))

		return OpenAPI()
			.addSecurityItem(securityRequirement)
			.components(components)
	}
}
