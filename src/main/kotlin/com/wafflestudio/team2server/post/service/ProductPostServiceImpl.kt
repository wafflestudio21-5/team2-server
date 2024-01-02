package com.wafflestudio.team2server.post.service

import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.post.repository.ProductPostRepository
import org.springframework.stereotype.Service
import com.wafflestudio.team2server.post.repository.ProductPostEntity

@Service
class ProductPostServiceImpl(
	private val productPostRepository: ProductPostRepository
): ProductPostService {
	override fun create() {
		TODO("Not yet implemented")
	}
	override fun searchPostByTitle(title: String): List<ProductPost> {
		return productPostRepository.findByTitleContaining(title).map(::ProductPost)
	}
}
fun ProductPost(it: ProductPostEntity): ProductPost = com.wafflestudio.team2server.post.model.ProductPost(
	id = it.id,
	authorId = it.authorId,
	buyerId = it.buyerId,
	chatCnt = it.chatCnt,
	createdAt = it.createdAt,
	deadline = it.deadline,
	hiddenYn = it.hiddenYn,
	offerYn = it.offerYn,
	refreshCnt = it.refreshCnt,
	refreshedAt = it.refreshedAt,
	repImg = it.repImg,
	title = it.title,
	viewCnt = it.viewCnt,
	wishCnt = it.wishCnt,
	type = it.type,
	status = it.status,
)
