package com.wafflestudio.team2server.auth

import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import kotlin.Unit as Unit

@RestController
class LoginController(
	private val authenticationManager: AuthenticationManager,
) {

	@PostMapping("/login")
	fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Authentication> {
		val authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username, loginRequest.password)
		val authenticationResponse = authenticationManager.authenticate(authenticationRequest)
		// TODO: 토큰 발급 후 리턴
		return ResponseEntity.ok().body(authenticationResponse)
	}

	data class LoginRequest(val username: String, val password: String)

	@ExceptionHandler(AuthenticationException::class)
	fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<Unit> {
		return ResponseEntity.status(HttpStatusCode.valueOf(403)).build()
	}

	@ExceptionHandler(HttpMessageConversionException::class)
	fun handleHttpMessageConversionException(e: HttpMessageConversionException): ResponseEntity<Unit> {
		return ResponseEntity.badRequest().build()
	}

}
