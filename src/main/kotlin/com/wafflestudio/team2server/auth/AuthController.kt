package com.wafflestudio.team2server.auth

import com.wafflestudio.team2server.common.auth.DatabaseUserDetailsService
import com.wafflestudio.team2server.common.auth.TokenGenerator
import com.wafflestudio.team2server.common.error.UserNotFoundException
import com.wafflestudio.team2server.user.repository.UserRepository
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
	private val userRepository: UserRepository,
) {

	@PostMapping("/login")
	fun login(@RequestBody loginRequest: LoginRequest): TokenResponse {
		val authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.email, loginRequest.password)
		val authenticationResponse = authenticationManager.authenticate(authenticationRequest)

		val user = (authenticationResponse.principal as? DatabaseUserDetailsService.UserWrapper)?.userEntity ?: throw UserNotFoundException

		val uid = authenticationResponse.name.toLong()
		val isAdmin = authenticationResponse.authorities.map { it.authority }.first() == "ADMIN"
		val refAreaIds = user.areaUsers.map { it.area.id }
		val token = tokenGenerator.create(uid, refAreaIds, isAdmin)
		return TokenResponse(uid, refAreaIds, isAdmin, token)
	}

	data class LoginRequest(
		val email: String,
		val password: String
	)

	data class TokenResponse(val uid: Long, val refAreaIds: List<Int>, val isAdmin: Boolean, val token: String)

	@ExceptionHandler(AuthenticationException::class)
	fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<Any> {
		return ResponseEntity(HttpStatus.UNAUTHORIZED)
	}

}
