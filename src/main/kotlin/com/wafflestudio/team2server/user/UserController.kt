package com.wafflestudio.team2server.user

import jakarta.validation.constraints.Pattern
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*

@RestController
class UserController {

	@PostMapping("/user/{providers}")
	fun signup(@RequestBody @Validated signupRequest: SignupRequest, @PathVariable providers: String) {

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
		@field:Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#\$%^&+=])(?=\\S+\$).{8,16}$", message = "비밀번호는 8~16자 영문 소문자, 숫자, 특수문자가 하나씩은 포함되어야 합니다.")
		val password: String,
		@field:Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
		val nickname: String,
	)

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleConstraintViolationException(e: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
		return ResponseEntity.badRequest().body(mapOf("message" to e.bindingResult.fieldError?.defaultMessage))
	}

}
