package com.wafflestudio.team2server.post.auction

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.post.model.PostCreateRequest
import com.wafflestudio.team2server.post.service.ProductPostServiceImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant

@SpringBootTest
class AuctionTest @Autowired constructor(
	val postService: ProductPostServiceImpl,
) {

	@Test
	fun `경매글 생성`() {
		postService.create(
			PostCreateRequest(
				1,
				"클로바 시계 - 재업",
				"클로바 시계 미개봉 상품 싸게 팔아요. 경매로 팝니다.",
				"AUCTION",
				"",
				listOf<String>(),
				false,
				Instant.now().plusMillis(10000000).toEpochMilli(),
				30000,
			),
			AuthUserInfo(
				3L,
				listOf(1, 2),
				false,
				null,
				null,
			)
		)
	}

	@Test
	fun `경매 가격 제시 - 3번`() {
		postService.bid(1L, 4L, 31000, Instant.now())
		postService.bid(7L, 4L, 30500, Instant.now())
		postService.bid(9L, 4L, 50000, Instant.now())
		println(postService.bidList(3L))
	}

	@Test
	fun `최소 가격보다 낮은 가격 제시`() {
		postService.bid(9L, 4L, 20000, Instant.now())
	}

	@Test
	fun `같은 가격 제시할 때 선착순으로 랭킹`() {
		postService.bid(1L, 4L, 31000, Instant.now())
		postService.bid(9L, 4L, 50000, Instant.now())
		postService.bid(7L, 4L, 30500, Instant.now())
		postService.bid(10L, 4L, 50000, Instant.now().plusMillis(10000))
		println(postService.bidList(4L))
	}

	@Test
	fun `가격 업데이트`() {
		postService.bid(1L, 4L, 51000, Instant.now())
		println(postService.bidList(4L))
	}

	@Test
	fun `BidSummary`() {
		println(postService.getAuctionPosts(10L))
	}

	@Test
	fun `게시물 조회`() {
		println(postService.getPostById(4L, AuthUserInfo(10L, listOf(1), false, null, null)))
	}

}
