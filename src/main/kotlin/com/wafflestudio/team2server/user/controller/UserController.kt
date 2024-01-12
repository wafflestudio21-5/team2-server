package com.wafflestudio.team2server.user.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.common.error.*
import com.wafflestudio.team2server.user.model.AuthProvider
import com.wafflestudio.team2server.user.model.User
import com.wafflestudio.team2server.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.constraints.Pattern
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
	private val userService: UserService,
) {

	@Operation(
		responses = [
			ApiResponse(responseCode = "200"),
			ApiResponse(responseCode = "400", description = "10001 INVALID_PARAMTER", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
			ApiResponse(responseCode = "409", description = "19000 CONFLICT", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
		]
	)
	@PostMapping("/signup")
	fun signup(@RequestBody @Validated request: SignupRequest): SignupResponse {
		if (request.refAreaIds.size !in 1..2) {
			throw InvalidAreaCountException
		}
		val user = userService.signup(
			request.email,
			request.password,
			request.nickname,
			request.profileImage,
			request.refAreaIds,
		)
		return SignupResponse(user)
	}

	@Operation(
		responses = [
			ApiResponse(responseCode = "200"),
			ApiResponse(responseCode = "400", description = "10001 INVALID_PARAMTER", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
			ApiResponse(responseCode = "409", description = "19000 CONFLICT", content = [Content(schema = Schema(implementation = ErrorResponse::class))])
		]
	)
	@PostMapping("/signup/{provider}")
	fun signupWithProvider(@RequestBody @Validated request: ProviderSignupRequest, @PathVariable provider: String): SignupResponse {
		if (request.refAreaIds.size !in 1..2) {
			throw InvalidAreaCountException
		}
		val user = userService.signupWithProvider(
			AuthProvider.valueOf(provider.uppercase()),
			request.nickname,
			request.profileImage,
			request.refAreaIds,
			request.idToken
		)
		return SignupResponse(user)
	}

	@PutMapping("/user")
	fun updateUser(@RequestBody @Validated request: UpdateUserRequest, @AuthenticationPrincipal user: AuthUserInfo): User {
		return userService.updateUser(user.uid, request)
	}

	@DeleteMapping("/user")
	fun deleteUser(@AuthenticationPrincipal user: AuthUserInfo) {
		userService.deleteUser(user.uid)
	}

	@GetMapping("/user")
	fun getUser(@AuthenticationPrincipal user: AuthUserInfo): User {
		return userService.getUser(user.uid)
	}

	data class SignupRequest(
		@field:Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
		val email: String,
		@field:Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&+=]).{8,}$", message = "비밀번호는 8~16자 영문 소문자, 숫자, 특수문자가 하나씩은 포함되어야 합니다.")
		val password: String,
		@field:Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
		val nickname: String,
		val profileImage: String?,
		val refAreaIds: List<Int>,
	)

	data class ProviderSignupRequest(
		@field:Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
		val nickname: String,
		val profileImage: String,
		val idToken: String,
		val refAreaIds: List<Int>,
	)

	data class UpdateUserRequest(
		@field:Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&+=]).{8,}$", message = "비밀번호는 8~16자 영문 소문자, 숫자, 특수문자가 하나씩은 포함되어야 합니다.")
		val password: String?,
		@field:Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
		val nickname: String?,
		val profileImage: String?,
	)

	data class SignupResponse(val user: User)

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleConstraintViolationException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
		return ResponseEntity(
			ErrorResponse(ErrorInfo(ErrorType.INVALID_PARAMETER.code, e.bindingResult.fieldError?.defaultMessage ?: "")),
			ErrorType.INVALID_PARAMETER.httpStatus,
		)
	}

}
