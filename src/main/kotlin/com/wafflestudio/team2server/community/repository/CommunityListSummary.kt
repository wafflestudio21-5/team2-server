package com.wafflestudio.team2server.community.repository

import java.time.Instant

interface CommunityListSummary {
	fun getId(): Long
	fun getTitle(): String
	fun getRepimg(): String
	fun getCreatedAt(): Instant?
	fun getViewCnt(): Int
	fun getLikeCnt(): Int
	fun getChatCnt(): Int
	fun getDescription(): String
	fun getAreaId(): Long
	fun getEnd(): Long
}
