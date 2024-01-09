package com.wafflestudio.team2server.user.repository

import com.wafflestudio.team2server.area.model.AreaUserEntity
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
	var password: String? = null,
	var role: User.Role = User.Role.USER,
	var profileImg: String?,
	var nickname: String,
	val sub: String? = null,
	var mannerTemperature: Double = 36.5,

	@OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
	var areaUsers: MutableList<AreaUserEntity> = mutableListOf(),
	@OneToMany(mappedBy = "user")
	val channelUsers: MutableList<ChannelUserEntity> = mutableListOf(),
):  BaseCreatedDateEntity()

