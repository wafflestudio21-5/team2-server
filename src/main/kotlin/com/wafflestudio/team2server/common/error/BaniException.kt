package com.wafflestudio.team2server.common.error

open class BaniException(val errorType: ErrorType) : RuntimeException(errorType.name)

object EmailBlankException : BaniException(ErrorType.EMAIL_BLANK)

object UserNotFoundException : BaniException(ErrorType.UNAUTHORIZED)

object EmailAlreadyExistsException : BaniException(ErrorType.EMAIL_ALREADY_EXISTS)

object NicknameAlreadyExistsException : BaniException(ErrorType.NICKNAME_ALREADY_EXISTS)

object ProviderKeyAlreadyExistsException : BaniException(ErrorType.PROVIDER_KEY_ALREAY_EXISTS)

object PostNotFoundException : BaniException(ErrorType.POST_NOT_FOUND)

object PermissionDeniedException : BaniException(ErrorType.PERMISSION_DENIED)

