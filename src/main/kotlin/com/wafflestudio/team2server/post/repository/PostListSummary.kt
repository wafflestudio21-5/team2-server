package com.wafflestudio.team2server.post.repository

import java.time.LocalDateTime

interface PostListSummary {
	fun getId(): Long
	fun getTitle(): String
	fun getRep_img(): String?
	fun getCreated_at(): LocalDateTime
	fun getRefreshed_at(): LocalDateTime?
	fun getChat_cnt(): Int
	fun getWish_cnt(): Int
	fun getSell_price(): Int
	fun getSelling_area_id(): Int
	fun getDeadline(): LocalDateTime
	fun getType(): String
	fun getStatus(): String
	fun getEnd(): Long?
}
