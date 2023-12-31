package com.wafflestudio.team2server.common.auth

import java.lang.RuntimeException

class AuthException(override val message: String?) : RuntimeException()
