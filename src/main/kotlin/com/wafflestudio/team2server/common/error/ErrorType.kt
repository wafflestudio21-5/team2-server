package com.wafflestudio.team2server.common.error

import org.springframework.http.HttpStatus

enum class ErrorType(
	val code: Int,
	val httpStatus: HttpStatus,
) {
	// 400
	INVALID_PARAMETER(10001, HttpStatus.BAD_REQUEST),
	EMAIL_BLANK(10004, HttpStatus.BAD_REQUEST),

	// 401
	UNAUTHORIZED(11001, HttpStatus.UNAUTHORIZED),

	// 403
	PERMISSION_DENIED(13001, HttpStatus.FORBIDDEN),

	// 404
	POST_NOT_FOUND(14001, HttpStatus.NOT_FOUND),

	// 409
	EMAIL_ALREADY_EXISTS(19001, HttpStatus.CONFLICT),
	NICKNAME_ALREADY_EXISTS(19002, HttpStatus.CONFLICT),
	PROVIDER_KEY_ALREAY_EXISTS(19003, HttpStatus.CONFLICT),
}
