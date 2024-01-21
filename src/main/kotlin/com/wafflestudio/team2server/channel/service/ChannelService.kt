package com.wafflestudio.team2server.channel.service

import com.wafflestudio.team2server.channel.repository.*
import com.wafflestudio.team2server.common.error.*
import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.post.repository.ProductPostEntity
import com.wafflestudio.team2server.post.repository.ProductPostRepository
import com.wafflestudio.team2server.user.repository.UserRepository
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant

private val logger: KLogger = KotlinLogging.logger { }

@Service
class ChannelService(
	private val channelUserRepository: ChannelUserRepository,
	private val channelRepository: ChannelRepository,
	private val userRepository: UserRepository,
	private val productPostRepository: ProductPostRepository,
	private val messageSequenceRepository: MessageSequenceRepository,
) {

	fun getList(userId: Long): ChannelListResponse {
		// 내가 포함된 채팅창 ID들 리스트 가져오기
		val myChannelUsers = channelUserRepository.findChannelsByUserId(userId).associateBy { it.channel.id }
		val channelIds = myChannelUsers.keys

		// 채팅창 ID 들을 기준으로 채팅방 정보, 채팅중인 상대방의 정보 가져오기
		val channelInfos = channelUserRepository.findChannelInfosByChannelIds(channelIds, userId)

		val channelFullList = channelInfos.map {
			ChannelBrief(
				channelId = it.channel.id,
				profileImg = it.user.profileImg,
				nickname = it.user.nickname,
				activeArea = it.channel.productPost.sellingArea.fullName,
				lastMsg = it.channel.lastMsg,
				msgUpdatedAt = it.channel.msgUpdatedAt,
				pinnedAt = myChannelUsers[it.channel.id]!!.pinnedAt
			)
		}

		val pinned = channelFullList.filter { it.pinnedAt != null }.sortedBy { it.pinnedAt }.reversed()
		val normal = channelFullList.filter { it.pinnedAt == null }.sortedBy { it.msgUpdatedAt }.reversed()

		return ChannelListResponse(pinned = pinned, normal = normal)

	}


	@Transactional
	fun createChannel(userId: Long, postId: Long): ChannelCreateResponse {

		// 1. postId로 해당 postEntity 가져오기
		val productPost = productPostRepository.findById(postId)
			.orElseThrow { PostIdNotFoundException }
		// 2. channel 존재여부 확인
		val channelO = channelRepository.findChannelByPostIdAndUserId(postId, userId)

		return if (channelO.isEmpty) {
			// 2-1. 존재하지 않는다면 -> channel_user 에 두 유저 추가
			createChannel(productPost, userId)
		} else {
			// 2-2. 존재한다면 -> 기존 채널 ID 반환
			ChannelCreateResponse(channelO.get().id)
		}
	}

	@Transactional
	fun pin(userId: Long, channelId: Long): ChannelPinResponse {
		val channelUserId = ChannelUserId(
			channelId = channelId,
			userId = userId,
		)
		val channelUser = channelUserRepository.findById(channelUserId)
			.orElseThrow { ChannelUserIdNotFoundException }

		val now = Instant.now()

		if (channelUser.pinnedAt != null) {
			throw AlreadyPinnedException
		}
		channelUser.pinnedAt = now

		return ChannelPinResponse(channelId, now)
	}

	@Transactional
	fun unpin(userId: Long, channelId: Long): ChannelUnpinResponse {
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
		return ChannelUnpinResponse(channelId)
	}

	private fun createChannel(productPost: ProductPostEntity, userId: Long): ChannelCreateResponse {
		val authorId = productPost.author.id
		if (userId == authorId) {
			throw SelfTransactionException
		}

		// 1. 채널 생성하기
		val channel = channelRepository.save(
			ChannelEntity(
				productPost = productPost
			)
		)

		logger.info {"channelId: ${channel.id}"}

		// 2. 채번 테이블 생성하기
		messageSequenceRepository.save(
			MessageSequenceEntity(
				channel = channel
			)
		)

		// 3. 구매자, 판매자(게시글 작성자) 등록하기
		saveChannelUser(userId, channel)
		saveChannelUser(authorId, channel)

		return ChannelCreateResponse(channel.id)
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

@Schema(description = "채팅방 요약 정보")
data class ChannelBrief(
	@Schema(description = "채팅방 식별자(ID)")
	val channelId: Long,
	@Schema(description = "채팅방 이미지(채팅 상대방의 프로필 이미지)")
	val profileImg: String?,
	@Schema(description = "채팅 상대방의 닉네임")
	val nickname: String,
	@Schema(description = "채팅 상대방의 활동 지역")
	val activeArea: String,
	@Schema(description = "해당 채팅방에 마지막으로 전송된 메시지 내용")
	val lastMsg: String?,
	@Schema(description = "해당 채팅방에 마지막으로 메시지가 전송된 시각")
	val msgUpdatedAt: Instant,
	@Schema(description = "채팅방이 고정된 시각. 고정되지 않았다면 null")
	val pinnedAt: Instant?
)

@Schema(description = "채팅 목록 응답 DTO")
data class ChannelListResponse(
	@Schema(description = "상단 고정된 채팅방들")
	val pinned: List<ChannelBrief>,
	@Schema(description = "고정되지 않은 일반 채팅방들")
	val normal: List<ChannelBrief>
)

@Schema(description = "채팅방 생성 응답 DTO")
data class ChannelCreateResponse(
	@Schema(description = "채팅방 식별자(ID)")
	val channelId: Long
)

@Schema(description = "채팅방 상단 고정 응답 DTO")
data class ChannelPinResponse(
	@Schema(description = "채팅방 식별자(ID)")
	val channelId: Long,
	@Schema(description = "채팅방 상단 고정 시각")
	val pinnedAt: Instant
)

@Schema(description = "채팅방 상단 고정 해제 응답 DTO")
data class ChannelUnpinResponse(
	@Schema(description = "채팅방 식별자(ID)")
	val channelId: Long
)








