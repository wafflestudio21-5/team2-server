package com.wafflestudio.team2server.user.model

data class TokenResponse(val uid: Long, val refAreaIds: List<Int>, val isAdmin: Boolean, val token: String)
