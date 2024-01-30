package com.wafflestudio.team2server.user.service

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.wafflestudio.team2server.area.model.Area
import com.wafflestudio.team2server.area.repository.*
import com.wafflestudio.team2server.common.auth.TokenVerifier
import com.wafflestudio.team2server.common.error.*
import com.wafflestudio.team2server.review.model.TradeReviewRequest
import com.wafflestudio.team2server.user.controller.UserController
import com.wafflestudio.team2server.user.model.AuthProvider
import com.wafflestudio.team2server.user.model.User
import com.wafflestudio.team2server.user.repository.UserEntity
import com.wafflestudio.team2server.user.repository.UserRepository
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.jvm.optionals.getOrElse

private val logger: KLogger = KotlinLogging.logger {}

@Service
class UserService(
	private val userRepository: UserRepository,
	private val areaUserRepository: AreaUserRepository,
	private val areaRepository: AreaRepository,
	private val tokenVerifier: TokenVerifier,
	private val passwordEncoder: PasswordEncoder,
) {

	@Transactional
	fun signup(email: String, password: String, nickname: String, profileImage: String?, refAreaIds: List<Int>): User {
		if (userRepository.existsByEmail(email)) {
			throw EmailAlreadyExistsException
		}
		if (userRepository.existsByNickname(nickname)) {
			throw NicknameAlreadyExistsException
		}
		val areas = areaRepository.findAllById(refAreaIds)
		if (areas.size != refAreaIds.size) {
			throw AreaNotFoundException
		}
		val user = userRepository.save(
			UserEntity(
				provider = AuthProvider.NONE,
				email = email,
				password = passwordEncoder.encode(password),
				profileImg = profileImage,
				nickname = nickname,
			)
		) // DB 유니크 키 제약으로 email, nickname 중복 방지.
		val areaUsers = areas.map { AreaUserEntity(AreaUserId(userId = user.id, areaId = it.id), it, user, count = 1) }
		areaUserRepository.saveAll(areaUsers)
		user.areaUsers.addAll(areaUsers)
		return user.toUser()
	}

	fun signupWithProvider(provider: AuthProvider, nickname: String, profileImage: String?, refAreaIds: List<Int>, idToken: String): User {
		val (sub, _) = try {
			tokenVerifier.verifyIdToken(provider, idToken)
		} catch (e: TokenExpiredException) {
			logger.warn { "open id token verifiation: expired: $e" }
			throw OpenIdTokenExpiredException
		} catch (e: JWTVerificationException) {
			logger.warn { "open id token verification: failed: $e" }
			throw OpenIdTokenVerificationException
		} catch (e: Exception) {
			logger.error { "open id token verification: error: $e" }
			throw OpenIdTokenVerificationException
		}
		if (userRepository.existsByProviderAndSub(provider, sub)) {
			throw ProviderKeyAlreadyExistsException
		}
		if (userRepository.existsByNickname(nickname)) {
			throw NicknameAlreadyExistsException
		}
		val areas = areaRepository.findAllById(refAreaIds)
		if (areas.size != refAreaIds.size) {
			throw AreaNotFoundException
		}
		val user = userRepository.save(
			UserEntity(
				provider = provider,
				nickname = nickname,
				profileImg = profileImage,
				sub = sub,
			)
		) // DB 유니크 키 제약으로 sub, nickname 중복 방지. Provider 사이에 sub 충돌 방지를 위해 (provider, sub)로 유니크 키 제약 필요할듯.
		val areaUsers = areas.map { AreaUserEntity(AreaUserId(userId = user.id, areaId = it.id), it, user, count = 1) }
		areaUserRepository.saveAll(areaUsers)
		user.areaUsers.addAll(areaUsers)
		return user.toUser()
	}

	fun checkDuplicateNickname(nickname: String): Boolean {
		if (userRepository.existsByNickname(nickname)) {
			throw NicknameAlreadyExistsException
		}
		return true
	}

	fun getUser(uid: Long): User {
		val user = userRepository.findByIdWithJoinFetch(uid) ?: throw UserNotFoundException
		return user.toUser()
	}

	@Transactional
	fun updateUser(uid: Long, request: UserController.UpdateUserRequest): User {
		val user = userRepository.findByIdWithJoinFetch(uid) ?: throw UserNotFoundException
		request.run {
			nickname?.let {
				if (userRepository.existsByNickname(nickname)) {
					throw NicknameAlreadyExistsException
				}
				user.nickname = it
			}
			password?.let { user.password = passwordEncoder.encode(it) }
			profileImage?.let { user.profileImg = it }
		}
		return user.toUser()
	}

	fun deleteUser(uid: Long) {
		userRepository.deleteById(uid)
	}

	/**
	 * TODO: 낙관적/비관적 락 적용해야할 듯.
	 */
	@Transactional
	fun addRefArea(uid: Long, refAreaId: Int): Int {
		val user = userRepository.getReferenceById(uid)
		val areaUsers = areaUserRepository.findByUser(user)
		val area = areaRepository.findById(refAreaId).getOrElse { throw AreaNotFoundException }

		val matchingAreaUser = areaUsers.find { it.id.areaId == area.id }
		if (matchingAreaUser != null) { // 인증 횟수 증가
			val authenticatedAt = matchingAreaUser.authenticatedAt
			val weekAgo = Instant.now().minus(7, ChronoUnit.DAYS)
			if (authenticatedAt == null || !authenticatedAt.isBefore(weekAgo)) {
				throw PermissionDeniedException
			}
			matchingAreaUser.count += 1
			matchingAreaUser.authenticatedAt = Instant.now()
		} else { // 새로운 ref 지역 등록
			if (areaUsers.size > 1) {
				throw InvalidAreaCountException
			}
			val newAreaUser = AreaUserEntity(AreaUserId(userId = uid, areaId = refAreaId), area, user, count = 1)
			areaUserRepository.save(newAreaUser)
		}
		return refAreaId
	}

	@Transactional
	fun deleteRefArea(uid: Long, refAreaId: Int) {
		val rows = areaUserRepository.deleteByUserIdAndAreaId(uid, refAreaId)
		if (rows == 0) {
			throw AreaNotFoundException
		}
	}

	@Transactional
	fun updateMannerTemperature(uid: Long, evaluation: TradeReviewRequest.Eval) {
		val user = userRepository.findById(uid).getOrElse { throw UserNotFoundException }
		user.mannerTemperature += evaluation.delta
	}

	private fun UserEntity.toUser() = User(
		id = id,
		email = email,
		provider = provider,
		sub = sub,
		role = role,
		profileImageUrl = profileImg,
		nickname = nickname,
		mannerTemp = mannerTemperature,
		createdAt = createdAt?.toEpochMilli(),
		refAreaIds = areaUsers.map { Area(it.area, it.authenticatedAt, it.count) },
	)

	private fun Area(entity: AreaEntity, authenticatedAt: Instant?, count: Int): Area = Area(
		entity.id, entity.code, entity.fullName, entity.name, entity.sggName, entity.sdName, authenticatedAt, count
	)

}
