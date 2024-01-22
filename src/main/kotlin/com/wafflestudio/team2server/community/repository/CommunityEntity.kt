package com.wafflestudio.team2server.community.repository

import com.wafflestudio.team2server.area.repository.AreaEntity
import com.wafflestudio.team2server.user.repository.UserEntity
import jakarta.persistence.*
import java.time.Instant

@Entity(name = "community")
class CommunityEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@OneToOne
	@JoinColumn(name = "user_id")
	val author: UserEntity,
	@OneToOne
	@JoinColumn(name = "area_id")
	val areaInfo: AreaEntity,
	val createdAt: Instant = Instant.now(),
	var title: String = "",
	var description: String = "",
	var viewCnt: Int = 0,
	var likeCnt: Int = 0,
	var chatCnt: Int = 0,
	var repImg: String = "",
)
