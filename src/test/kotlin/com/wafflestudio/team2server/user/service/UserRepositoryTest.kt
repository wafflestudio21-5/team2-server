package com.wafflestudio.team2server.user.service

import com.wafflestudio.team2server.user.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserRepositoryTest @Autowired constructor(
	val userRepository: UserRepository,
){

	@Test
	fun findByEmail() {
		val user = userRepository.findByEmail("youngin4@naver.com")
	}

}
