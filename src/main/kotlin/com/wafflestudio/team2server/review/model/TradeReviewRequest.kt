package com.wafflestudio.team2server.review.model

import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType

data class TradeReviewRequest(
	val receiverId: Long,
	val description: String,
	val evaluation: Int, // 0: 불만족, 1: 보통, 2: 만족
	val authorAreaId: Int,
) {
	fun getEval(): Eval {
		return when (evaluation) {
			0 -> Eval.POOR
			1 -> Eval.FAIR
			2 -> Eval.GOOD
			else -> throw BaniException(ErrorType.INVALID_PARAMETER)
		}
	}

	enum class Eval(val delta: Double) {
		POOR(-0.1),
		FAIR(0.0),
		GOOD(0.1),
	}
}
