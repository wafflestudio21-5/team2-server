package com.wafflestudio.team2server.user.service

import com.wafflestudio.team2server.area.model.AreaUserEntity
import com.wafflestudio.team2server.area.model.AreaUserId
import com.wafflestudio.team2server.area.repository.AreaRepository
import com.wafflestudio.team2server.area.repository.AreaUserRepository
import com.wafflestudio.team2server.common.error.*
import com.wafflestudio.team2server.user.controller.UserController
import com.wafflestudio.team2server.user.model.AuthProvider
import com.wafflestudio.team2server.user.model.User
import com.wafflestudio.team2server.user.repository.UserEntity
import com.wafflestudio.team2server.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrElse

@Service
class UserService(
	private val userRepository: UserRepository,
	private val areaUserRepository: AreaUserRepository,
	private val areaRepository: AreaRepository,
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
		return user.toUser()
	}

	fun signupWithProvider(provider: AuthProvider, nickname: String, profileImage: String?, sub: String, refAreaIds: List<Int>): User {
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
		return user.toUser()
	}

	fun getUser(uid: Long): User {
		val user = userRepository.findById(uid).getOrElse { throw UserNotFoundException }
		return user.toUser()
	}

	@Transactional
	fun updateUser(uid: Long, request: UserController.UpdateUserRequest): User {
		val user = userRepository.findById(uid).getOrElse { throw UserNotFoundException }
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

	private fun UserEntity.toUser() = User(
		id = id,
		email = email,
		provider = provider,
		sub = sub,
		role = role,
		profileImageUrl = profileImg,
		nickname = nickname,
		mannerTemp = mannerTemperature,
		createdAt = createdAt,
		refAreaIds = emptyList(),
	)

}
