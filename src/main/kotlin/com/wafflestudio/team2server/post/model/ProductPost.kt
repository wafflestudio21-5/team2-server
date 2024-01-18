package com.wafflestudio.team2server.post.model

data class ProductPost(
	val id: Long,
	val title: String,
	val description: String,
	val type: String,
	val status: String,
	val authorId: Long,
	val authorName: String,
	val buyerId: Long,
	val sellingArea: String,
	val sellPrice: Int,
	val repImg: String,
	val images: List<String>,
	val viewCnt: Int,
	val offerYn: Boolean,
	val refreshCnt: Int,
	val refreshedAt: Long,
	val createdAt: Long,
	val deadline: Long,
	val wishCnt: Int,
	val chatCnt: Int,
	// val trading_location: LocationPointEntity,
	val isWish: Boolean,
	// val mannerTemperature: Int,
	// val categoryId: Long,
	// val category: String,
	val profileImg: String,
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
