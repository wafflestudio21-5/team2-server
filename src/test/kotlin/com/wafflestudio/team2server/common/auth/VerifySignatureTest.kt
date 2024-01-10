package com.wafflestudio.team2server.common.auth

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.junit.jupiter.api.Test
import java.security.interfaces.RSAPublicKey
import java.util.concurrent.TimeUnit

class VerifySignatureTest {

	@Test
	fun `공개키로 서명 검증`() {
		val signature =
			"eyJraWQiOiI5ZjI1MmRhZGQ1ZjIzM2Y5M2QyZmE1MjhkMTJmZWEiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIyZjA4NTNlNWZjNzg0ZTkwZjBlYmM2NTJmYjYxNmE5NyIsInN1YiI6IjMyNzgyOTUyOTMiLCJhdXRoX3RpbWUiOjE3MDQ5MDA5NTIsImlzcyI6Imh0dHBzOi8va2F1dGgua2FrYW8uY29tIiwibmlja25hbWUiOiLshpDsmIHspIAiLCJleHAiOjE3MDQ5NDQxNTIsImlhdCI6MTcwNDkwMDk1MiwicGljdHVyZSI6Imh0dHA6Ly9rLmtha2FvY2RuLm5ldC9kbi9LQ0xtOC9idHJUVG96YmZ4Vy9nd1hxSUxkajIwQ1BUOXpxMnpBTGJrL2ltZ18xMTB4MTEwLmpwZyJ9.G72R9ljMP9L0tf9LITxV4e1VkN4OzaNQd-ReqsTFoHAPzy_bX3flY2qsEusiRD9YW3ohdd1QROzO7DdhKR8QJlOKOMPvjW6CZjcjen2I2Zi7NoSFO2JamMM7VenNR2i0BtPTzFdUOFVkwTjOj7aiAXN6N3y8e541HPOVkkr0OX2JUsQ2TXppgzTa9c7Wnj1cl8Ruh8PcG4_NDUy_xFsusanZV_-Z8_0VcS1LFc1C7LlqbEg3mDaiMTn7HvZSMX3rS6nbVjFaTHRSScNsmsGykQuIW1hn2Ns3hKClDf9qq_KYfI5bP5ugW5DSWaqlXuCr7aYKP8vFTJWlyhS4PpecWQ"

		val provider = JwkProviderBuilder("https://kauth.kakao.com")
			.cached(10, 7, TimeUnit.DAYS)
			.build()

		val jwk = provider.get("9f252dadd5f233f93d2fa528d12fea")

		val algo = Algorithm.RSA256(jwk.publicKey as RSAPublicKey)
		val verifier = JWT.require(algo).build()

		verifier.verify(signature)
	}

}
