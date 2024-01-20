package com.wafflestudio.team2server.channel.service

import com.wafflestudio.team2server.channel.repository.ChannelMessageEntity
import com.wafflestudio.team2server.channel.repository.ChannelMessageRepository
import com.wafflestudio.team2server.channel.repository.ChannelUserEntity
import com.wafflestudio.team2server.channel.repository.ChannelUserRepository
import com.wafflestudio.team2server.post.model.ProductPost
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import java.time.LocalDateTime

@Service
class ChannelDetailService(
	private val channelMessageRepository: ChannelMessageRepository,
	private val channelUserRepository: ChannelUserRepository,
	private val webSocketSessionManager: WebSocketSessionManager,
) {

	fun getRecentMessage(channelId: Long, session: WebSocketSession, headers: Map<String, String>) {
		val cur = headers["cur"]?.toLong()

		val messages = mutableListOf<ChannelMessageEntity>()
		val userId = webSocketSessionManager.findUserIdFromSession(session)
		var channelInfo: ChannelInfo? = null

		// 1. 메시지 이력 찾기 (무한 스크롤)
		// case 1. 가장 최신 메시지 이력 찾기
		if (cur == null) {
			messages.addAll(channelMessageRepository.findRecentMessages(channelId))

			channelInfo = ChannelInfo(
				channelUserRepository.findChannelInfo(channelId, userId)
			)

		}
		// case 2. cursor 기준으로 메시지 이력 찾기
		else {
			messages.addAll(channelMessageRepository.findRecentMessagesWithCursor(channelId, cur))
		}

//		ChannelDetailResponse(
//			channelInfo = channelInfo,
//			messages = messages.map {
//						ChannelMessage(
//							msgNo = it.msgNo,
//							isMine = (it.sender.id  == userId),
//							createdAt =
//
//						)
//			},
//			cur = messages.last().id - 1
//		)


		// 2. 메시지 이력 요청 보낸 사람에게 보내기
//			session.sendMessage()

	}

	fun sendText(channelId: Long, msg: String) {
		// TODO 1. 메시지 저장 & 전송 (각각 다른 스레드로 처리하기)
		//

		// 1-1. 메시지 저장

		// 1-2. 메시지 전송
		// 상세 페이지 전송
		// 목록 페이지 전송


		// 2. 상대방의 목록 세션이 연결되어 있을 경우, 거기에도 새 메시지가 왔다고 보내기
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
	val isMine: Long,
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
