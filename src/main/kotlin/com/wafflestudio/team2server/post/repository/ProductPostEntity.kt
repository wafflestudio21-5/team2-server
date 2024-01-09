package com.wafflestudio.team2server.post.repository

import com.wafflestudio.team2server.area.model.AreaEntity
import com.wafflestudio.team2server.post.model.ProductPost.ProductPostStatus
import com.wafflestudio.team2server.post.model.ProductPost.ProductPostType
import com.wafflestudio.team2server.user.repository.UserEntity
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity(name = "product_post")
class ProductPostEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	var title: String = "",
	var description: String = "",
	@Enumerated(value = EnumType.STRING)
	var type: ProductPostType = ProductPostType.TRADE,
	@Enumerated(value = EnumType.STRING)
	var status: ProductPostStatus = ProductPostStatus.NEW,
	@OneToOne
	@JoinColumn(name = "author_id")
	val author: UserEntity,
	var buyerId: Long = -1,
	var sellPrice: Int,
	@OneToOne
	@JoinColumn(name = "selling_area_id")
	val sellingArea: AreaEntity,
	var repImg: String = "",
	// val trading_location: LocationPointEntity,
	var viewCnt: Int = 0,
	var offerYn: Boolean = false,
	var refreshCnt: Int = 0,
	var refreshedAt: LocalDateTime = LocalDateTime.now(),
	val createdAt: LocalDateTime = LocalDateTime.now(),
	var deadline: LocalDateTime = LocalDateTime.now(),
	var hiddenYn: Boolean = false,
	var wishCnt: Int = 0,
	var chatCnt: Int = 0,
)
