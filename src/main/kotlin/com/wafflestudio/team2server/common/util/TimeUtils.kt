package com.wafflestudio.team2server.common.util

import java.time.LocalDateTime
import java.time.ZoneId

fun LocalDateTime.toEpochMillis(): Long = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
