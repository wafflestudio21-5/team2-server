package com.wafflestudio.team2server.common.util

import org.junit.jupiter.api.Test

class MessageUtilTest {
	private val messageUtil = MessageUtil()

	private val text1 = """
		RECENT_MESSAGE
		cursor:1

	""".trimIndent()

	private val text2 = """
		SEND_TEXT

		Hello World
		My Name is Kotlin!!
	""".trimIndent()

	@Test
	fun `이전 대화내역 받기 파싱 확인`() {
		messageUtil.messageParser(text1)
	}

	@Test
	fun `채팅 보개니 파싱 확인`() {
		messageUtil.messageParser(text2)
	}
}
