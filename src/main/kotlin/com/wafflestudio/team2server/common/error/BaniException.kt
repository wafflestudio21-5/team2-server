package com.wafflestudio.team2server.common.error

open class BaniException(val errorType: ErrorType) : RuntimeException(errorType.name)

object BadRequestException : BaniException(ErrorType.INVALID_PARAMETER)

object InvalidAreaCountException : BaniException(ErrorType.INVALID_AREA_COUNT)

object EmailBlankException : BaniException(ErrorType.EMAIL_BLANK)

object UserNotFoundException : BaniException(ErrorType.USER_NOT_FOUND)

object AreaNotFoundException : BaniException(ErrorType.AREA_NOT_FOUND)

object EmailAlreadyExistsException : BaniException(ErrorType.EMAIL_ALREADY_EXISTS)

object NicknameAlreadyExistsException : BaniException(ErrorType.NICKNAME_ALREADY_EXISTS)

object OpenIdTokenVerificationException : BaniException(ErrorType.UNAUTHORIZED)

object OpenIdTokenExpiredException : BaniException(ErrorType.EXPIRED_TOKEN)

object InvalidProviderException : BaniException(ErrorType.INVALID_PROVIDER)

object ProviderKeyAlreadyExistsException : BaniException(ErrorType.PROVIDER_KEY_ALREADY_EXISTS)

object NoUIDException : BaniException(ErrorType.NO_UID)

object PostIdNotFoundException : BaniException(ErrorType.POST_ID_NOT_FOUND)

object ChannelUserIdNotFoundException : BaniException(ErrorType.CHANNEL_USER_ID_NOT_FOUND)

object AlreadyPinnedException : BaniException(ErrorType.ALREADY_PINNED)

object NotPinnedException : BaniException(ErrorType.NOT_PINNED)

object SelfTransactionException : BaniException(ErrorType.SELF_TRANSACTION)

object PostNotFoundException : BaniException(ErrorType.POST_NOT_FOUND)

object CommunityNotFoundException : BaniException(ErrorType.COMMUNITY_NOT_FOUND)

object PermissionDeniedException : BaniException(ErrorType.PERMISSION_DENIED)

object FileNotAvailableException : BaniException(ErrorType.FILE_NOT_AVAILABLE)

object BadFileFormatException : BaniException(ErrorType.BAD_FILE_FORMAT)
