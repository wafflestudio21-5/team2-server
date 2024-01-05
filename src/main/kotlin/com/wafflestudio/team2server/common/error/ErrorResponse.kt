package com.wafflestudio.team2server.common.error

data class ErrorResponse(
	val error: ErrorInfo
)

data class ErrorInfo(
	val code: Int,
	val message: String,
)
