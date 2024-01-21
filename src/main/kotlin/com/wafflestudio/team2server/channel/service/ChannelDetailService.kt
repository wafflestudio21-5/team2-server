package com.wafflestudio.team2server.channel.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.wafflestudio.team2server.channel.repository.*
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.post.model.ProductPost
import com.wafflestudio.team2server.user.repository.UserRepository
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

private val logger: KLogger = KotlinLogging.logger { }

@Service
class ChannelDetailService(
	private val channelMessageRepository: ChannelMessageRepository,
	private val channelUserRepository: ChannelUserRepository,
	private val channelRepository: ChannelRepository,
	private val userRepository: UserRepository,
	private val messageSequenceRepository: MessageSequenceRepository,
	private val sessionManager: WebSocketSessionManager,
	private val objectMapper: ObjectMapper,
) {

	fun getRecentMessage(channelId: Long, session: WebSocketSession, headers: Map<String, String>) {
		val cur = headers["cur"]?.toLong()

		val messages = mutableListOf<ChannelMessageEntity>()
		val myUserId = sessionManager.findUserIdFromChannelSession(session)
		var channelInfo: ChannelInfo? = null

		// 1. 메시지 이력 찾기 (무한 스크롤)
		// case 1. 가장 최신 메시지 이력 찾기
		if (cur == null) {
			messages.addAll(channelMessageRepository.findRecentMessages(channelId))

			channelInfo = ChannelInfo(
				channelUserRepository.findChannelInfo(channelId, myUserId)
			)

		}
		// case 2. cursor 기준으로 메시지 이력 찾기
		else {
			messages.addAll(channelMessageRepository.findRecentMessagesWithCursor(channelId, cur))
		}

		val channelDetailResponse = ChannelDetailResponse(
			channelInfo = channelInfo,
			messages = messages.map {
						ChannelMessage(
							msgNo = it.msgNo,
							isMine = (it.sender.id  == myUserId),
							createdAt = it.createdAt?.toEpochMilli()?: 0, // 0이 될 일은 없겠지만..
							message = it.message
						)
			},
			cur = messages.last().id - 1
		)

		// 2. 메시지 이력 조회를 요청한 사람에게 보내기
			session.sendMessage(
				TextMessage(objectMapper.writeValueAsString(channelDetailResponse))
			)
	}

	@Transactional
	fun sendText(channelId: Long, session: WebSocketSession, msg: String) {
		val myUserId = sessionManager.findUserIdFromChannelSession(session)

		// 1. 메시지 저장
		val savedChannelMessage = saveMessages(channelId, myUserId, msg)

		// 2. 메시지 전송
		val channelUsers = sessionManager.findChannelUsers(channelId)

		// 2-1. 상세 페이지 전송
		for (channelUserId in channelUsers) {
			try {
				val channelSession = sessionManager.findDetailSessionFromUserId(channelUserId)
				channelSession.sendMessage(
					TextMessage(
						objectMapper.writeValueAsString(
							ChannelMessage(
								msgNo = savedChannelMessage.msgNo,
								message = savedChannelMessage.message,
								createdAt = savedChannelMessage.createdAt?.toEpochMilli()?: 0,
								isMine = (myUserId == sessionManager.findUserIdFromChannelSession(channelSession))
							)
						)
					)
				)
			} catch (e: BaniException) {
				logger.info { "채팅 상세 미연결 [userId: $channelUserId]" }
			}
		}

		// 2-2. 상대방의 목록 세션이 연결되어 있을 경우, 거기에도 새 메시지가 왔다고 보내기
		for (channelUserId in channelUsers) {
			if (channelUserId == myUserId) {
				continue
			}
			try {
				val otherSession = sessionManager.findSessionFromUserId(channelUserId)
				otherSession.sendMessage(
					TextMessage(
						objectMapper.writeValueAsString(
							ChannelMessageBrief(
								channelId = channelId,
								createdAt = savedChannelMessage.createdAt?.toEpochMilli()?: 0,
								message = savedChannelMessage.message
							)
						)
					)
				)
			} catch (e: BaniException) {
				logger.info { "채팅 목록 미연결 [userId: $channelUserId]" }
			}
		}
	}

	fun saveMessages(channelId: Long, myUserId: Long, msg: String): ChannelMessageEntity {
		val channel = channelRepository.findById(channelId).get()
		val sender = userRepository.getReferenceById(myUserId)

		// 1-1. 시퀀스 테이블 값 1 증가
		val msgSeq = messageSequenceRepository.findById(channelId).get()
		msgSeq.nextMsgNo += 1

		// 1-2. 메시지 저장
		val savedChannelMessage = channelMessageRepository.save(
			ChannelMessageEntity(
				channel = channel,
				sender = sender,
				message = msg,
				msgNo = msgSeq.nextMsgNo
			)
		)

		// 1-3. 메시지 목록 update
		channel.lastMsg = msg
		channel.msgUpdatedAt = savedChannelMessage.createdAt!!

		return savedChannelMessage
	}
}

data class ChannelDetailResponse(
	@Schema(description = "메시지 내용")
	val messages: List<ChannelMessage>,
	@Schema(description = "메시지 번호 중 가장 작은 번호")
	val cur: Long,
	@Schema(description = "채팅방 정보")
	val channelInfo: ChannelInfo?
)

@Schema(description = "채팅 메시지")
data class ChannelMessage(
	@Schema(description = "메시지 번호")
	val msgNo: Long,
	@Schema(description = "누가 보낸 메시지인지를 표시. true: 내 메시지, false: 상대방 메시지")
	val isMine: Boolean,
	@Schema(description = "메시지 내용")
	val message: String,
	@Schema(description = "메시지 발송 시각")
	val createdAt: Long,
)

@Schema(description = "채팅방 정보")
data class ChannelInfo(
	@Schema(description = "채팅 상대방 닉네임")
	val nickname: String,
	@Schema(description = "채팅 상대방 프로필 사진")
	val profileImg: String?,
	@Schema(description = "판매 물품 사진")
	val repImg: String,
	@Schema(description = "게시글 제목")
	val title: String,
	@Schema(description = "판매 가격")
	val sellPrice: Int,
	@Schema(description = "물품 상태 - NEW(신규), RESERVED(예약), SOLDOUT(판매완료)")
	val status: ProductPost.ProductPostStatus
)

data class ChannelMessageBrief(
	@Schema(description = "채팅 ID")
	val channelId: Long,
	@Schema(description = "메시지 내용")
	val message: String,
	@Schema(description = "메시지 발송 시각")
	val createdAt: Long,
)

private fun ChannelInfo(entity: ChannelUserEntity): ChannelInfo {
	return ChannelInfo(
		nickname = entity.user.nickname,
		profileImg = entity.user.profileImg,
		repImg = entity.channel.productPost.repImg,
		title = entity.channel.productPost.title,
		sellPrice = entity.channel.productPost.sellPrice,
		status = entity.channel.productPost.status,
	)
}
