package com.wafflestudio.team2server

import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorInfo
import com.wafflestudio.team2server.common.error.ErrorResponse
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

private val logger: KLogger = KotlinLogging.logger {}

@RestControllerAdvice
class ErrorHandler {

	@ExceptionHandler(
		ConstraintViolationException::class,
		HttpMessageNotReadableException::class,
		HttpMediaTypeNotSupportedException::class,
		HttpRequestMethodNotSupportedException::class,
		MethodArgumentNotValidException::class,
		MethodArgumentTypeMismatchException::class,
		MissingServletRequestParameterException::class,
	)
	fun handleHttpMessageBadRequest(
		e: Exception,
		request: HttpServletRequest,
		response: HttpServletResponse,
	): ResponseEntity<Any> {
		return ResponseEntity(HttpStatus.BAD_REQUEST)
	}

	@ExceptionHandler(
		ObjectOptimisticLockingFailureException::class,
	)
	fun handleHttpMessageConflict(
		e: Exception,
		request: HttpServletRequest,
		response: HttpServletResponse,
	): ResponseEntity<Any> {
		return ResponseEntity(HttpStatus.CONFLICT)
	}

	@ExceptionHandler(Exception::class)
	fun handlerException(e: Exception): ResponseEntity<Any> {
		e.printStackTrace()
		return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
	}

	@ExceptionHandler(BaniException::class)
	fun handlerBaniException(e: BaniException): ResponseEntity<ErrorResponse> {
		return ResponseEntity(
			ErrorResponse(ErrorInfo(e.errorType.code, e.errorType.name)),
			e.errorType.httpStatus,
		)
	}

}
