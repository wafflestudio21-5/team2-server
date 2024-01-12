package com.wafflestudio.team2server.common.auth

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.wafflestudio.team2server.common.error.InvalidProviderException
import com.wafflestudio.team2server.common.error.NoUIDException
import com.wafflestudio.team2server.common.error.OpenIdTokenVerificationException
import com.wafflestudio.team2server.user.model.AuthProvider
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.interfaces.RSAPublicKey
import java.util.Base64
import java.util.concurrent.TimeUnit

private val logger: KLogger = KotlinLogging.logger {}

/**
 * 토큰 검증
 */
@Component
class TokenVerifier(
	@Value("\${jwt.secret-key}") private val secretKey: String,
	@Value("\${kakao.service-id}") private val kakaoServiceId: String,
) {

	private val decoder: Base64.Decoder = Base64.getUrlDecoder()

	private val secretKeyBytes = decoder.decode(secretKey)

	private val algorithm: Algorithm = Algorithm.HMAC256(secretKeyBytes)

	private val jwtVerifier: JWTVerifier = initVerifier()

	fun verifyJWT(token: String): AuthUserInfo {
		val decodedJWT = jwtVerifier.verify(token)
		return createAuthUserInfo(decodedJWT)
	}

	fun verifyIdToken(provider: AuthProvider, idToken: String): Pair<String, Long> {
		return when (provider) {
			AuthProvider.KAKAO -> {
				val decoded = JWT.decode(idToken)
				if (kakaoServiceId !in decoded.audience) {
					throw OpenIdTokenVerificationException
				}
				val jwkProvider = JwkProviderBuilder(decoded.issuer)
					.cached(10, 7, TimeUnit.DAYS)
					.build()

				val jwk = jwkProvider.get(decoded.keyId)
				val algo = Algorithm.RSA256(jwk.publicKey as RSAPublicKey)
				val idTokenVerifier = JWT.require(algo).build()

				if (decoded.subject == null) {
					logger.debug { "open id token doesn't have sub" }
					throw OpenIdTokenVerificationException
				}
				idTokenVerifier.verify(decoded)
				Pair(decoded.subject, decoded.expiresAtAsInstant.toEpochMilli())
			}
			else -> { throw InvalidProviderException }
		}
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
