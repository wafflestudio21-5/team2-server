package com.wafflestudio.team2server.common.error

import org.springframework.http.HttpStatus

enum class ErrorType(
	val code: Int,
	val httpStatus: HttpStatus,
) {
	// 400
	EMAIL_BLANK(10004, HttpStatus.BAD_REQUEST),

	// 401
	UNAUTHORIZED(11001, HttpStatus.UNAUTHORIZED),

	// 403

	// 404

	// 409
	EMAIL_ALREADY_EXISTS(19001, HttpStatus.CONFLICT),
	NICKNAME_ALREADY_EXISTS(19002, HttpStatus.CONFLICT),
	PROVIDER_KEY_ALREAY_EXISTS(19003, HttpStatus.CONFLICT),
}
