package com.wafflestudio.team2server.user.controller

import com.wafflestudio.team2server.user.model.AuthProvider
import com.wafflestudio.team2server.user.service.UserService
import jakarta.validation.constraints.Pattern
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
	private val userService: UserService,
	private val passwordEncoder: PasswordEncoder,
) {

	@PostMapping("/signup")
	fun signup(@RequestBody @Validated request: SignupRequest): SignupResponse {
		val user = userService.signup(
			request.email,
			passwordEncoder.encode(request.password),
			request.nickname,
			request.profileImage,
		)
		return SignupResponse(user.id)
	}

	@PostMapping("/signup/{provider}")
	fun signupWithProvider(@RequestBody @Validated request: ProviderSignupRequest, @PathVariable provider: String): SignupResponse {
		val user = userService.signupWithProvider(
			AuthProvider.valueOf(provider),
			request.nickname,
			request.profileImage,
			request.sub,
		)
		return SignupResponse(user.id)
	}

	@PutMapping("/user")
	fun update() {

	}

	@DeleteMapping("/user")
	fun delete() {

	}

	@GetMapping("/user")
	fun get() {

	}

	data class SignupRequest(
		@field:Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
		val email: String,
		@field:Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&+=]).{8,}$", message = "비밀번호는 8~16자 영문 소문자, 숫자, 특수문자가 하나씩은 포함되어야 합니다.")
		val password: String,
		@field:Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
		val nickname: String,
		val profileImage: String?,
	)

	data class ProviderSignupRequest(
		@field:Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
		val nickname: String,
		val profileImage: String,
		val sub: String,
	)

	data class SignupResponse(val id: Long)

}
