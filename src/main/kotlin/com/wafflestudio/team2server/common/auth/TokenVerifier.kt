package com.wafflestudio.team2server.common.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.wafflestudio.team2server.common.error.NoUIDException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Base64

/**
 * 토큰 검증
 */
@Component
class TokenVerifier(
	@Value("\${jwt.secret-key}") private val secretKey: String,
) {

	private val decoder: Base64.Decoder = Base64.getUrlDecoder()

	private val secretKeyBytes = decoder.decode(secretKey)

	private val algorithm: Algorithm = Algorithm.HMAC256(secretKeyBytes)

	private val verifier: JWTVerifier = initVerifier()

	fun createAuthUserInfo(token: String): AuthUserInfo {
		val decodedJWT = verifier.verify(token)
		return createAuthUserInfo(decodedJWT)
	}

	private fun createAuthUserInfo(token: DecodedJWT): AuthUserInfo {
		val uid = token.getClaim("uid")?.asLong()?: throw NoUIDException
		val rawRefAreaIds = token.getClaim("ra")?.asList(Int::class.java) ?: emptyList()
		val isAdmin = token.getClaim("admin")?.asBoolean() ?: false
		val issuedAt = token.issuedAtAsInstant?.toEpochMilli()
		val expiredAt = token.expiresAtAsInstant?.toEpochMilli()
		return AuthUserInfo(uid, rawRefAreaIds, isAdmin, issuedAt, expiredAt)
	}

	private fun initVerifier(): JWTVerifier {
		return JWT.require(algorithm)
			.withClaim("v", 1)
			.withClaimPresence("uid")
			.withClaimPresence("iat")
			.build()
	}

}
