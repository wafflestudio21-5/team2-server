package com.wafflestudio.team2server.post.controller

import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.post.service.ProductPostService
import com.wafflestudio.team2server.user.model.User
import org.springframework.web.bind.annotation.*

@RestController
class ProductPostController(private val productPostService: ProductPostService) {
	@GetMapping("/posts")
	fun postList(){
		println("hey")
	}
	@PostMapping("/posts")
	fun postProduct() {
		productPostService.create()
	}
	@GetMapping("/posts/{id}")
	fun getPost(@PathVariable id: Long){
		return
	}
	@PostMapping("/posts/{id}")
	fun likePost(@PathVariable id: Long, user: User){

	}
	@GetMapping("/posts/search/{keyword}")
	fun searchPost(@PathVariable keyword: String): List<ProductPost> {
		return productPostService.searchPostByTitle(keyword)
	}
}
