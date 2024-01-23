package com.wafflestudio.team2server.review.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.review.model.TradeReview
import com.wafflestudio.team2server.review.model.TradeReviewRequest
import com.wafflestudio.team2server.review.service.TradeReviewService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class TradeReviewController(
	private val tradeReviewService: TradeReviewService
) {
	@PostMapping("/posts/review/{post_id}")
	fun createTradeReview(
		@PathVariable("post_id") postId: Long,
		@RequestBody request: TradeReviewRequest,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	) {
		tradeReviewService.createTradeReview(postId, request, authUserInfo)
	}

	@GetMapping("/user/{user_id}/review")
	fun getTradeReview(
		@RequestParam("user_id") userId: Long,
		@RequestParam("from") from: String, // all, sender, buyer 중 하나
		@AuthenticationPrincipal authUserInfo: AuthUserInfo
	): List<TradeReview> {
		return tradeReviewService.getTradeReviewFrom(userId, from)
	}
}
