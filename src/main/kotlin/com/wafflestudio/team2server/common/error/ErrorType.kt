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
	NO_UID(11002, HttpStatus.UNAUTHORIZED),

	// 403

	// 404
	POST_ID_NOT_FOUND(14001, HttpStatus.NOT_FOUND),
	CHANNEL_USER_ID_NOT_FOUND(14002, HttpStatus.NOT_FOUND),

	// 409
	EMAIL_ALREADY_EXISTS(19001, HttpStatus.CONFLICT),
	NICKNAME_ALREADY_EXISTS(19002, HttpStatus.CONFLICT),
	PROVIDER_KEY_ALREADY_EXISTS(19003, HttpStatus.CONFLICT),
	ALREADY_PINNED(19004, HttpStatus.CONFLICT),
	NOT_PINNED(19005, HttpStatus.CONFLICT),
	SELF_TRANSACTION(19006, HttpStatus.CONFLICT)
}
