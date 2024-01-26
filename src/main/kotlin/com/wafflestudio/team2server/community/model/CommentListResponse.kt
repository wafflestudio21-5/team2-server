package com.wafflestudio.team2server.community.model

import java.time.Instant
import javax.swing.text.StyledEditorKit.BoldAction

data class CommentListResponse (
	val id: Long,
	val nickname: String,
	val comment: String,
	val imgUrl: String,
	val createdAt: Instant?,
	val likeCnt: Int,
	// val areaId: Long,
	val isLiked: Boolean,
	val childComments: List<CommentSummary> = emptyList()
)
