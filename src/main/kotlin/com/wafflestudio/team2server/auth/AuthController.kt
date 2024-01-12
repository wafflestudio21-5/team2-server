package com.wafflestudio.team2server.auth

import com.wafflestudio.team2server.user.model.AuthProvider
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*

private val logger: KLogger = KotlinLogging.logger {}

@RestController
@RequestMapping("/auth")
class AuthController(
	private val authService: AuthService
) {

	@PostMapping("/login")
	fun login(@RequestBody loginRequest: LoginRequest): TokenResponse {
		return authService.login(loginRequest.email, loginRequest.password)
	}

	@PostMapping("/login/{provider}")
	fun socialLogin(@PathVariable provider: String, @RequestBody request: SocialLoginRequest): TokenResponse {
		return authService.socialLogin(AuthProvider.valueOf(provider.uppercase()), request.idToken)
	}

	data class LoginRequest(
		val email: String,
		val password: String
	)

	data class SocialLoginRequest(
		val idToken: String,
	)

	data class TokenResponse(val uid: Long, val refAreaIds: List<Int>, val isAdmin: Boolean, val token: String)

	@ExceptionHandler(AuthenticationException::class)
	fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<Any> {
		return ResponseEntity(HttpStatus.UNAUTHORIZED)
	}

}
