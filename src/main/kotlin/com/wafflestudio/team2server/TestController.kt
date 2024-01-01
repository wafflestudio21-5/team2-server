package com.wafflestudio.team2server

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger: KLogger = KotlinLogging.logger { }

@RestController
@RequestMapping("/test")
class TestController {

	@GetMapping("/auth")
	fun testAuth(@AuthenticationPrincipal authUserInfo: AuthUserInfo): String {
		val result = """
			<test auth result>
			name = ${authUserInfo.uid},
			refAreaIds = ${authUserInfo.refAreaIds},
			isAdmin = ${authUserInfo.isAdmin},
			issuedAt = ${authUserInfo.issuedAt},
			expiredAt = ${authUserInfo.expiredAt}
		""".trimIndent()
		logger.debug { result }
		return result
	}

}
