package com.wafflestudio.team2server.common.error

import java.lang.RuntimeException

open class BaniException(val errorType: ErrorType) : RuntimeException(errorType.name)

object EmailBlankException : BaniException(ErrorType.EMAIL_BLANK)

object UserNotFoundException : BaniException(ErrorType.UNAUTHORIZED)

object EmailAlreadyExistsException : BaniException(ErrorType.EMAIL_ALREADY_EXISTS)

object NicknameAlreadyExistsException : BaniException(ErrorType.NICKNAME_ALREADY_EXISTS)

object ProviderKeyAlreadyExistsException : BaniException(ErrorType.PROVIDER_KEY_ALREADY_EXISTS)

object NoUIDException : BaniException(ErrorType.NO_UID)

object PostIdNotFoundException : BaniException(ErrorType.POST_ID_NOT_FOUND)

object ChannelUserIdNotFoundException : BaniException(ErrorType.CHANNEL_USER_ID_NOT_FOUND)

object AlreadyPinnedException : BaniException(ErrorType.ALREADY_PINNED)

object NotPinnedException : BaniException(ErrorType.NOT_PINNED)

object SelfTransactionException : BaniException(ErrorType.SELF_TRANSACTION)
