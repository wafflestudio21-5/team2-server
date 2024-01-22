package com.wafflestudio.team2server.post.repository

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository

/**
 * 경매 관련 저장소.
 *
 * Redis 이용.
 */
@Repository
class AuctionRepository(
	redisTemplate: StringRedisTemplate,
) {

	companion object {
		const val BID_LIST_PREFIX = "bl"

		const val DEADLINE_AUCTION_PREFIX = "da"

		const val USER_AUCTION_LIST_PREFIX = "ua"

		const val DIGIT = 10000000
	}

	private val zSetOperations = redisTemplate.opsForZSet()

	private val setOperation = redisTemplate.opsForSet()

	fun bid(postId: Long, userId: Long, score: Double) {
		zSetOperations.add("$BID_LIST_PREFIX:$postId", userId.toString(), score)
		setOperation.add("$USER_AUCTION_LIST_PREFIX:$userId", postId.toString())
	}

	fun getTopRankBidList(postId: Long, rank: Long): Map<Long, Int> {
		val top10 = zSetOperations.reverseRangeWithScores("$BID_LIST_PREFIX:$postId", 0, rank)
		return top10?.associate {
			val value = it.value?.toLongOrNull()
			val score = it.score
			if (value == null || score == null) {
				return emptyMap()
			}
			val bidPrice = (score / DIGIT).toInt() * 100
			value to bidPrice
		} ?: emptyMap()
	}

	fun deleteBidList(postId: Long) {
		zSetOperations.remove("$BID_LIST_PREFIX:$postId")
	}

	fun getAuctionPosts(userId: Long): List<Long> {
		return setOperation.members("$USER_AUCTION_LIST_PREFIX:$userId")
			?.mapNotNull { it.toLongOrNull() } ?: emptyList()
	}

}
