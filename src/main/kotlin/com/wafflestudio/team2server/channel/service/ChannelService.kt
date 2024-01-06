package com.wafflestudio.team2server.channel.service

import com.wafflestudio.team2server.channel.repository.*
import com.wafflestudio.team2server.common.error.*
import com.wafflestudio.team2server.post.repository.ProductPostEntity
import com.wafflestudio.team2server.post.repository.ProductPostRepository
import com.wafflestudio.team2server.user.repository.UserEntity
import com.wafflestudio.team2server.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChannelService(
	private val channelUserRepository: ChannelUserRepository,
	private val channelRepository: ChannelRepository,
	private val userRepository: UserRepository,
	private val productPostRepository: ProductPostRepository,
) {

	fun getList(userId: Long):ChannelListResponse {
		// 내가 포함된 채팅창 ID들 리스트 가져오기
		val myChannelUsers = channelUserRepository.findChannelIdsByUserId(userId).associateBy { it.channel.id }
		val channelIds = myChannelUsers.keys

		// 채팅창 ID 들을 기준으로 채팅방 정보, 채팅중인 상대방의 정보 가져오기
		val channelInfos = channelUserRepository.findChannelInfosByChannelIds(channelIds, userId)

		val channelFullList = channelInfos.map {
			ChannelBrief(
				channelId = it.channel.id,
				profileImg = it.user.profileImg,
				nickname = it.user.nickname,
				activeArea = "추후 수정", // TODO 추후 수정
				lastMsg = it.channel.lastMsg,
				msgUpdatedAt = it.channel.msgUpdatedAt,
				pinnedAt = myChannelUsers[it.channel.id]!!.pinnedAt
			)
		}

		val pinned = channelFullList.filter { it.pinnedAt != null } .sortedBy { it.pinnedAt } .reversed()
		val normal = channelFullList.filter { it.pinnedAt == null } .sortedBy { it.msgUpdatedAt } .reversed()

		return ChannelListResponse(pinned = pinned, normal = normal)

	}

	@Transactional
	fun createChannel(userId: Long, postId: Long): CreateChannelResponse {

		println("단계 1")
		// 1. postId로 해당 postEntity 가져오기
		val productPost = productPostRepository.findById(postId)
			.orElseThrow { PostIdNotFoundException }
		println("단계 2")
		// 2. channel 존재여부 확인
		val channelO = channelRepository.findChannelByPostIdAndUserId(postId, userId)

		return if (channelO.isEmpty) {
			println("단계 3")
			// 2-1. 존재하지 않는다면 -> channel_user 에 두 유저 추가
			createChannel(productPost, userId)
		} else {
			// 2-2. 존재한다면 -> 기존 채널 ID 반환
			CreateChannelResponse(channelO.get().id)
		}
	}

	@Transactional
	fun pin(userId: Long, channelId: Long): PinResponse {
		val channelUserId = ChannelUserId(
			channelId = channelId,
			userId = userId,
		)
		val channelUser = channelUserRepository.findById(channelUserId)
			.orElseThrow { ChannelUserIdNotFoundException }

		val now = LocalDateTime.now()

		if (channelUser.pinnedAt != null) {
			throw AlreadyPinnedException
		}
		channelUser.pinnedAt = now

		return PinResponse(channelId, now)
	}

	@Transactional
	fun unpin(userId: Long, channelId: Long): UnpinResponse {
		val channelUserId = ChannelUserId(
			channelId = channelId,
			userId = userId,
		)
		val channelUser = channelUserRepository.findById(channelUserId)
			.orElseThrow { ChannelUserIdNotFoundException }

		if (channelUser.pinnedAt == null) {
			throw NotPinnedException
		}
		channelUser.pinnedAt = null
		return UnpinResponse(channelId)
	}

	private fun createChannel(productPost: ProductPostEntity, userId: Long): CreateChannelResponse {
		val authorId = productPost.authorId
		if (userId == authorId) {
			throw SelfTransactionException
		}

		// 1. 채널 생성하기
		val channel = channelRepository.save(
			ChannelEntity(
				productPost = productPost
			)
		)
		// 2. 구매자, 판매자(게시글 작성자) 등록하기
		saveChannelUser(userId, channel)
		saveChannelUser(authorId, channel)

		return CreateChannelResponse(channel.id)
	}


	private fun saveChannelUser(userId: Long, channel: ChannelEntity) {

//		// TODO("getReferenceById를 쓰는데도 user 쪽에 SELECT 쿼리가 자꾸 나감")
		val user = userRepository.getReferenceById(userId)
		val channelUserId = ChannelUserId(
			channelId = channel.id,
			userId = userId
		)

		channelUserRepository.save(
			ChannelUserEntity(
				id = channelUserId,
				channel = channel,
				user = user
			)
		)
	}
}

data class ChannelBrief(
	val channelId: Long,
	val profileImg: String?,
	val nickname: String,
	val activeArea: String,
	val lastMsg: String?,
	val msgUpdatedAt: LocalDateTime,
	val pinnedAt: LocalDateTime?
)

data class ChannelListResponse(
	val pinned: List<ChannelBrief>,
	val normal: List<ChannelBrief>
)


data class CreateChannelResponse(
	val channelId: Long
)

data class PinResponse(
	val channelId: Long,
	val pinnedAt: LocalDateTime
)

data class UnpinResponse(
	val channelId: Long
)
