package com.wafflestudio.team2server.post.repository

import java.time.Instant

interface PostListSummary {
	fun getId(): Long
	fun getTitle(): String
	fun getRep_img(): String
	fun getCreated_at(): Instant
	fun getRefreshed_at(): Instant?
	fun getChat_cnt(): Int
	fun getWish_cnt(): Int
	fun getSell_price(): Int
	fun getSelling_area_id(): Int
	fun getDeadline(): Instant
	fun getType(): String
	fun getStatus(): String
	fun getEnd(): Long?
}
