package com.wafflestudio.team2server.common.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

/**
 *  토큰 생성하는 클래스.
 */
@Component
class TokenGenerator(
	@Value("\${jwt.secret-key}") private val secretKey: String,
) {

	private val secretKeyBytes = Base64.getUrlDecoder().decode(secretKey)

	private val algorithm: Algorithm = Algorithm.HMAC256(secretKeyBytes)

	/**
	 * 토큰 생성.
	 */
	fun create(uid: Long, refAreaIds: List<Int>, isAdmin: Boolean, expiredAtEpochMillis: Long = 1000 * 3600 * 6): String {
		val issuedAt = Instant.now()
		val jwt = try {
			JWT.create()
				.withClaim("v", 1)
				.withClaim("uid", uid)
				.withClaim("ra", refAreaIds)
				.withClaim("admin", isAdmin)
				.withIssuedAt(issuedAt)
				.sign(algorithm)
		} catch (e: JWTCreationException) {
			throw AuthException("JWT creation: failed: $e")
		}
		return jwt
	}

}
