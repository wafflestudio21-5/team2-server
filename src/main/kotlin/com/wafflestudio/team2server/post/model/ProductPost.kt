package com.wafflestudio.team2server.post.model

import com.wafflestudio.team2server.area.model.AreaEntity
import java.time.LocalDateTime

data class ProductPost(
	val id: Long? = null,
	val title: String,
	val type: ProductPostType = ProductPostType.TRADE,
	val status: ProductPostStatus = ProductPostStatus.NEW,
	val authorId: Long,
	val buyerId: Long,
	val sellingArea: AreaEntity,
	val repImg: String,
	// val trading_location: LocationPointEntity,
	val viewCnt: Int,
	val offerYn: Boolean,
	val refreshCnt: Int,
	val refreshedAt: LocalDateTime,
	val createdAt: LocalDateTime,
	val deadline: LocalDateTime,
	val hiddenYn: Boolean,
	val wishCnt: Int,
	val chatCnt: Int,
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
