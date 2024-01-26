package com.wafflestudio.team2server.community.repository

import java.time.Instant

interface CommunityListSummary {
	fun getId(): Long
	fun getTitle(): String
	fun getRep_img(): String
	fun getCreated_at(): Instant?
	fun getView_cnt(): Int
	fun getLike_cnt(): Int
	fun getChat_cnt(): Int
	fun getDescription(): String
	fun getArea_info(): String
	fun getEnd(): Long
}
