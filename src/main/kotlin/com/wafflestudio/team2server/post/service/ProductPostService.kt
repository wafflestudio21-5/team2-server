package com.wafflestudio.team2server.post.service

import com.wafflestudio.team2server.post.model.ProductPost
import java.util.*

interface ProductPostService {
	fun searchPostByTitle(title: String): List<ProductPost>
	fun create()
}
