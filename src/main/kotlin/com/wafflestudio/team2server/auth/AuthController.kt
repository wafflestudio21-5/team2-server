package com.wafflestudio.team2server.auth

import com.wafflestudio.team2server.common.auth.TokenGenerator
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.*

private val logger: KLogger = KotlinLogging.logger {}

@RestController
@RequestMapping("/auth")
class AuthController(
	private val authenticationManager: AuthenticationManager,
	private val tokenGenerator: TokenGenerator,
) {

	@PostMapping("/login")
	fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<TokenResponse> {
		val authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.email, loginRequest.password)
		val authenticationResponse = authenticationManager.authenticate(authenticationRequest)

		val uid = authenticationResponse.name.toLong()
		val rawRefAreaIds = authenticationResponse.authorities.map { it.authority }
		val token = tokenGenerator.create(uid, rawRefAreaIds)
		return ResponseEntity.ok().body(TokenResponse(uid, rawRefAreaIds, token))
	}

	data class LoginRequest(
		val email: String,
		val password: String
	)

	data class TokenResponse(val uid: Long, private val rawRefAreaIds: List<String>, val token: String) {

		val refAreaIds: List<Int>
			get() = rawRefAreaIds.mapNotNull(String::toIntOrNull)

		val isAdmin: Boolean
			get() = rawRefAreaIds.any { it == "ADMIN" }

	}

	@ExceptionHandler(AuthenticationException::class)
	fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<Unit> {
		return ResponseEntity.status(HttpStatusCode.valueOf(403)).build()
	}

	@ExceptionHandler(HttpMessageConversionException::class)
	fun handleHttpMessageConversionException(e: HttpMessageConversionException): ResponseEntity<Unit> {
		return ResponseEntity.badRequest().build()
	}

}
