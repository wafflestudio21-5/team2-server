package com.wafflestudio.team2server.common.util

import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class MessageUtil {

	fun messageParser(payload: String): MessageResult {

		val lines = payload.lines()

		// COMMAND 추출
		val command = lines.firstOrNull()?.trim()?:throw BaniException(ErrorType.INVALID_MESSAGE_FORMAT)

		// 헤더(key-value 쌍) 추출
		val headerLines = lines.drop(1).takeWhile { it.isNotBlank() }
		val headerPairs = headerLines.associate {
			val (key, value) = it.split(":").map { it.trim() }
			key to value
		}

		// BODY 추출
		val bodyLines = lines.dropWhile { it.isNotBlank() }.drop(1)
		val body = bodyLines.joinToString("\n").trim()

		// 추출된 데이터 출력
		println("Command: $command")
		println("Headers: $headerPairs")
		println("Body: $body")

		return MessageResult(
			command = command,
			headers = headerPairs,
			body = body
		)
	}

	fun getChannelId(session: WebSocketSession): Long {
		val path = session.uri?.path?: throw RuntimeException()
		return path.substring(path.lastIndexOf("/") + 1).toLong()
	}

	fun getQueryParams(session: WebSocketSession): HashMap<String, String> {
		val query = session.uri?.query?: throw BaniException(ErrorType.UNAUTHORIZED)
		val params = query.split("&")

		val map = HashMap<String, String>()

		for (param in params) {
			val name = param.split("=")[0]
			val value = param.split("=")[1]
			map[name] = value
		}
		return map
	}
}

data class MessageResult(
	val command: String,
	val headers: Map<String, String>,
	val body: String
)
