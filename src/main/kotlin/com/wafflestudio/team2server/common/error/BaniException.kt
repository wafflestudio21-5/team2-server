package com.wafflestudio.team2server.common.error

import java.lang.RuntimeException

open class BaniException(val errorType: ErrorType) : RuntimeException(errorType.name)

object BadRequestException : BaniException(ErrorType.INVALID_PARAMETER)

object InvalidAreaCountException : BaniException(ErrorType.INVALID_AREA_COUNT)

object EmailBlankException : BaniException(ErrorType.EMAIL_BLANK)

object UserNotFoundException : BaniException(ErrorType.USER_NOT_FOUND)

object AreaNotFoundException : BaniException(ErrorType.AREA_NOT_FOUND)

object EmailAlreadyExistsException : BaniException(ErrorType.EMAIL_ALREADY_EXISTS)

object NicknameAlreadyExistsException : BaniException(ErrorType.NICKNAME_ALREADY_EXISTS)

object ProviderKeyAlreadyExistsException : BaniException(ErrorType.PROVIDER_KEY_ALREAY_EXISTS)
