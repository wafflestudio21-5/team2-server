package com.wafflestudio.team2server.user.repository

import com.wafflestudio.team2server.channel.repository.ChannelUserEntity
import com.wafflestudio.team2server.common.util.BaseCreatedDateEntity
import com.wafflestudio.team2server.user.model.AuthProvider
import com.wafflestudio.team2server.user.model.User
import jakarta.persistence.*

@Entity(name = "user")
class UserEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0L,
	@Enumerated(value = EnumType.ORDINAL)
	val provider: AuthProvider,
	val email: String? = null,
	val password: String? = null,
	val role: User.Role = User.Role.USER,
	val profileImg: String?,
	val nickname: String,
	val sub: String? = null,
	var mannerTemperature: Double = 36.5,

	@OneToMany(mappedBy = "user")
	val channelUsers: List<ChannelUserEntity> = mutableListOf(),
):  BaseCreatedDateEntity()

