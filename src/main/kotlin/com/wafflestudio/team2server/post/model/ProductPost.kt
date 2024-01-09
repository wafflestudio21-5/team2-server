package com.wafflestudio.team2server.post.model

import java.time.LocalDateTime

data class ProductPost(
	val id: Long,
	val title: String,
	val description: String,
	val type: Int,
	val status: Int,
	val authorId: Long,
	val buyerId: Long,
	val sellingArea: String,
	val sellPrice: Int,
	val repImg: String,
	val images: List<String>,
	val viewCnt: Int,
	val offerYn: Boolean,
	val refreshCnt: Int,
	val refreshedAt: LocalDateTime,
	val createdAt: LocalDateTime,
	val deadline: LocalDateTime,
	val hiddenYn: Boolean,
	val wishCnt: Int,
	val chatCnt: Int,
	// val trading_location: LocationPointEntity,
) {
	enum class ProductPostStatus {
		NEW,
		RESERVED,
		SOLDOUT,
	}

	enum class ProductPostType {
		TRADE,
		SHARE,
		AUCTION
	}
}
