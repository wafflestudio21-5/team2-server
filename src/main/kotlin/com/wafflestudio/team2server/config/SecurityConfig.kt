package com.wafflestudio.team2server.config

import com.wafflestudio.team2server.common.auth.JwtHs256AuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

/**
 * Spring Security 설정.
 *
 * @EnableWebSecurity debug 변수를 true로 설정하면 필터 순서 등 더 자세한 정보를 알 수 있다.
 */
@Configuration
@EnableWebSecurity(debug = false)
class SecurityConfig {

	@Bean
	fun securityFilterChain(http: HttpSecurity, jwtHs256AuthFilter: JwtHs256AuthFilter): SecurityFilterChain {
		http {
			csrf { disable() }
			addFilterAfter<BasicAuthenticationFilter>(jwtHs256AuthFilter)
			authorizeHttpRequests {
				authorize("/login", permitAll)
				authorize(anyRequest, authenticated)
			}
		}
		return http.build()
	}

	@Bean
	fun authenticationManager(
		userDetailsService: UserDetailsService,
		passwordEncoder: PasswordEncoder,
	): AuthenticationManager {
		val authenticationProvider = DaoAuthenticationProvider()
		authenticationProvider.setUserDetailsService(userDetailsService)
		authenticationProvider.setPasswordEncoder(passwordEncoder)
		return ProviderManager(authenticationProvider)
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}

}
