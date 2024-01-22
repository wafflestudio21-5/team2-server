package com.wafflestudio.team2server.user.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
class UserServiceTest @Autowired constructor(
	val userService: UserService,
	val passwordEncoder: PasswordEncoder,
) {

	@Test
	fun `회원가입 테스트`() {
		userService.signup(
			email = "bani1245@naver.com",
			password = passwordEncoder.encode("1234"),
			nickname = "baniixdxe",
			profileImage = "ddd",
			refAreaIds = listOf(2),
		)
	}

	@Test
	fun `회원 삭제 테스트`() {
		// TODO: delete할 때 select 쿼리도 같이 나감.
		userService.deleteUser(11)
	}

	@Test
	fun `매너온도 업데이트 테스트`() {
		userService.updateMannerTemperature(33, 0.1)
	}

}
