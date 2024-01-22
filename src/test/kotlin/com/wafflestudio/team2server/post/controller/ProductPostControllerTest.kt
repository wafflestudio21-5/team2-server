package com.wafflestudio.team2server.post.controller

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders


@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class ProductPostControllerTest(private val mvc: MockMvc) {
	@Test
	fun infScrollTest() {
		mvc.perform(
			MockMvcRequestBuilders.get("/posts")
				.param("cur", "1")
				.param("seed", "0")
				.param("distance", "1")
				.param("count", "0")
		)
	}
}
