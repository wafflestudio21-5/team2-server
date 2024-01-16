package com.wafflestudio.team2server.user.controller

import com.wafflestudio.team2server.user.service.AuthService
import com.wafflestudio.team2server.user.model.AuthProvider
import com.wafflestudio.team2server.user.model.TokenResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*

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

	@ExceptionHandler(AuthenticationException::class)
	fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<Any> {
		return ResponseEntity(HttpStatus.UNAUTHORIZED)
	}

}
