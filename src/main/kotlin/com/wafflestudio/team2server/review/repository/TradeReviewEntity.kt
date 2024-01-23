package com.wafflestudio.team2server.review.repository

import com.wafflestudio.team2server.area.repository.AreaEntity
import com.wafflestudio.team2server.common.util.BaseCreatedDateEntity
import com.wafflestudio.team2server.post.repository.ProductPostEntity
import com.wafflestudio.team2server.user.repository.UserEntity
import jakarta.persistence.*

@Entity(name = "trade_review")
class TradeReviewEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	val post: ProductPostEntity? = null,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	val sender: UserEntity? = null,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	val receiver: UserEntity? = null,
	val description: String? = null,

	@Enumerated(EnumType.STRING)
	val authorType: AuthorType? = null,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_area_id")
	val authorArea: AreaEntity? = null,

	// val rating: Int = 0
) : BaseCreatedDateEntity() {
	enum class AuthorType {
		BUYER, SELLER
	}
}
