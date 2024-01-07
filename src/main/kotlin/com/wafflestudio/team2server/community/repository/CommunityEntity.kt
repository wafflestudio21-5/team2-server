package com.wafflestudio.team2server.community.repository

import com.wafflestudio.team2server.community.model.Community.*
import com.wafflestudio.team2server.user.repository.UserEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name="community")
class CommunityEntity (
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,
	@OneToOne
	@JoinColumn(name = "user_id")
	val author: UserEntity,
	val areaId: Long = -1,
	val createdAt: LocalDateTime = LocalDateTime.now(),
	val title: String = "",
	val description: String = "",
	val viewCnt: Int = 0,
	val likeCnt: Int = 0,
	val repImg: String = "",
	@Enumerated(value = EnumType.ORDINAL)
	val status: CommunityStatus = CommunityStatus.CREATED,
)
