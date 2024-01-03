package com.wafflestudio.team2server.user.repository

import com.wafflestudio.team2server.channel.repository.ChannelUserEntity
import com.wafflestudio.team2server.common.util.BaseCreatedDateEntity
import com.wafflestudio.team2server.user.model.User
import jakarta.persistence.*

@Entity(name = "user")
class UserEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long = 0L,
	@Enumerated(value = EnumType.ORDINAL)
	val provider: UserProvider,
	val role: User.Role,
	val profileImg: String,
	val nickname: String,
	val sub: String?,
	var mannerTemperature: Float,

	@OneToMany(mappedBy = "user")
	val channelUsers: List<ChannelUserEntity>
):  BaseCreatedDateEntity() {

	enum class UserProvider {
		KAKAO, GOOGLE, NONE
	}
}

