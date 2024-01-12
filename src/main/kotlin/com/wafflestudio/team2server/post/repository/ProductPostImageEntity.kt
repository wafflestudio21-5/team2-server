package com.wafflestudio.team2server.post.repository

import jakarta.persistence.*
import java.time.Instant

@Entity(name = "product_img")
class ProductPostImageEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	val url: String,
	@ManyToOne
	@JoinColumn(name = "post_id")
	val productPost: ProductPostEntity,
	val createdAt: Instant = Instant.now(),
)
