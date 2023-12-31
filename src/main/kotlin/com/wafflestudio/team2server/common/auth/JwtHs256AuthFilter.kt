package com.wafflestudio.team2server.common.auth

import com.auth0.jwt.exceptions.JWTVerificationException
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

private val log: KLogger = KotlinLogging.logger {}

@Component
class JwtHs256AuthFilter(
	private val tokenVerifier: TokenVerifier,
) : OncePerRequestFilter() {

	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
		val authentication = authenticate(request) ?: UsernamePasswordAuthenticationToken.unauthenticated(null, null)
		SecurityContextHolder.getContext().authentication = authentication
		filterChain.doFilter(request, response)
	}

	private fun authenticate(request: HttpServletRequest): Authentication? {
		return try {
			val authHeader = request.getHeader("authorization")
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return null
			}
			val token = authHeader.substring(7)
			val authUserInfo = tokenVerifier.createAuthUserInfo(token)
			val authorities = mutableListOf(SimpleGrantedAuthority("user"))
			if (authUserInfo.isAdmin) {
				authorities.add(SimpleGrantedAuthority("admin"))
			}
			UsernamePasswordAuthenticationToken.authenticated(authUserInfo, null, authorities)
		} catch (e: JWTVerificationException) {
			log.error { "JWT verification: failed: $e" }
			null
		} catch (e: Exception) {
			log.error { "JWT verification: error: $e" }
			null
		}
	}

}
