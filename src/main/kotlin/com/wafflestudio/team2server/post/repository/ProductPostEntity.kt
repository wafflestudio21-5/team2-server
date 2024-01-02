package com.wafflestudio.team2server.post.repository

import jakarta.persistence.*
import java.time.LocalDateTime
import com.wafflestudio.team2server.user.model.User
import com.wafflestudio.team2server.post.model.ProductPost.*


@Entity(name="product_post")
class ProductPostEntity (
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	val title: String = "",
	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	val type: ProductPostType = ProductPostType.TRADE,
	val status: ProductPostStatus = ProductPostStatus.NEW,
	val authorId: Long = 0,
	val buyerId: Long = -1,
	//val selling_area: AreaEntity,
	val repImg: String = "",
	// val trading_location: LocationPointEntity,
	val viewCnt: Int = 0,
	val offerYn: Boolean = false,
	val refreshCnt: Int = 0,
	val refreshedAt: LocalDateTime = LocalDateTime.now(),
	val createdAt: LocalDateTime = LocalDateTime.now(),
	val deadline: LocalDateTime = LocalDateTime.now(),
	val hiddenYn: Boolean = false,
	val wishCnt: Int = 0,
	val chatCnt: Int = 0,
)
