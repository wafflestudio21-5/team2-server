package com.wafflestudio.team2server.channel.service

import com.wafflestudio.team2server.channel.repository.ChannelUserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
class ChannelService(
	private val channelUserRepository: ChannelUserRepository,
) {

	fun getList(userId: Long):List<ChannelBrief> {
		// 나의 채팅 목록에 들어갈 채널들과, 각 채널들이 어떤 물품에 대한 것인지를 가져옴
		val channelUserInfos = channelUserRepository.findAllByUserIdWithJoinFetch(userId).associateBy { it.channel.id }

		val channelIds: Set<Long> = channelUserInfos.keys

		// 채널 아이디들을 통해, 각 채팅의 상대방에 대한 정보를 가져옴

//		val userInfos = channelUserRepository.findUserInfosByChannelId(channelIds, userId)
//			.sortedBy { it. }
//
//		channelUserInfos.map { ChannelBrief(
//					profileImg = it.
//
//				)
//			}
		TODO()
	}

}

data class ChannelBrief(
	val profileImg: String,
	val nickname: String,
	val activeArea: String,
	val lastMsg: String?,
	val msgUpdatedAt: LocalDateTime?,
	val pinnedAt: LocalDateTime?
)
