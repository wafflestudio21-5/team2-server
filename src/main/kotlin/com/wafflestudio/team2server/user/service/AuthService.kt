package com.wafflestudio.team2server.user.service

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.wafflestudio.team2server.common.auth.DatabaseUserDetailsService
import com.wafflestudio.team2server.common.auth.TokenGenerator
import com.wafflestudio.team2server.common.auth.TokenVerifier
import com.wafflestudio.team2server.common.error.OpenIdTokenExpiredException
import com.wafflestudio.team2server.common.error.OpenIdTokenVerificationException
import com.wafflestudio.team2server.common.error.UserNotFoundException
import com.wafflestudio.team2server.user.model.AuthProvider
import com.wafflestudio.team2server.user.model.TokenResponse
import com.wafflestudio.team2server.user.model.User
import com.wafflestudio.team2server.user.repository.UserRepository
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

private val logger: KLogger = KotlinLogging.logger {}

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val tokenGenerator: TokenGenerator,
    private val tokenVerifier: TokenVerifier,
    private val userRepository: UserRepository,
) {

	fun login(email: String, password: String): TokenResponse {
		val authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(email, password)
		val authenticationResponse = authenticationManager.authenticate(authenticationRequest)

		val user = (authenticationResponse.principal as? DatabaseUserDetailsService.UserWrapper)?.userEntity ?: throw UserNotFoundException

		val uid = authenticationResponse.name.toLong()
		val isAdmin = authenticationResponse.authorities.map { it.authority }.first() == "ADMIN"
		val refAreaIds = user.areaUsers.map { it.area.id }
		val token = tokenGenerator.create(uid, refAreaIds, isAdmin)
		return TokenResponse(uid, refAreaIds, isAdmin, token)
	}

	fun socialLogin(provider: AuthProvider, idToken: String): TokenResponse {
		val (sub, expiredAt) = try {
			tokenVerifier.verifyIdToken(provider, idToken)
		} catch (e: TokenExpiredException) {
			logger.warn { "open id token verifiation: expired: $e" }
			throw OpenIdTokenExpiredException
		} catch (e: JWTVerificationException) {
			logger.warn { "open id token verification: failed: $e" }
			throw OpenIdTokenVerificationException
		} catch (e: Exception) {
			logger.error { "open id token verification: error: $e" }
			throw OpenIdTokenVerificationException
		}

		val user = userRepository.findByProviderAndSubWithJoinFetch(provider, sub) ?: throw UserNotFoundException

		val uid = user.id
		val isAdmin = user.role == User.Role.ADMIN
		val refAreaIds = user.areaUsers.map { it.area.id }
		val token = tokenGenerator.create(uid, refAreaIds, isAdmin, expiredAt)
		return TokenResponse(uid, refAreaIds, isAdmin, token)
	}

}
