package com.wafflestudio.team2server.community.repository

import com.wafflestudio.team2server.community.model.Community.CommunityStatus
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
	val areaId: Long = -1,
	val createdAt: Instant = Instant.now(),
	var title: String = "",
	var description: String = "",
	var viewCnt: Int = 0,
	var likeCnt: Int = 0,
	var repImg: String = "",
	@Enumerated(value = EnumType.ORDINAL)
	var status: CommunityStatus = CommunityStatus.CREATED,
)
