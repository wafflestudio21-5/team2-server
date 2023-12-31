package com.wafflestudio.team2server.common.auth

import org.junit.jupiter.api.Test
import java.security.SecureRandom
import java.util.*

class TokenGeneratorTest {

	private val base64Encoder: Base64.Encoder = Base64.getUrlEncoder().withoutPadding()

	@Test
	fun generateSecretKey() {
		val random = SecureRandom()
		val randomBytes = ByteArray(32)
		random.nextBytes(randomBytes)
		println(base64Encoder.encodeToString(randomBytes))
	}

}
