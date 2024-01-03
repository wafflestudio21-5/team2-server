package com.wafflestudio.team2server.user.service

import com.wafflestudio.team2server.user.model.AuthProvider
import com.wafflestudio.team2server.user.repository.UserEntity
import com.wafflestudio.team2server.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
	private val userRepository: UserRepository,
) {

	fun signup(email: String, password: String, nickname: String, profileImage: String?): UserEntity {
		return userRepository.save(UserEntity(
			provider = AuthProvider.NONE,
			email = email,
			password = password,
			profileImg = profileImage,
			nickname = nickname,
		)) // DB 유니크 키 제약으로 email, nickname 중복 방지.
	}

	fun signupWithProvider(provider: AuthProvider, nickname: String, profileImage: String?, sub: String?): UserEntity {
		return userRepository.save(UserEntity(
			provider = provider,
			nickname = nickname,
			profileImg = profileImage,
			sub = sub,
		)) // DB 유니크 키 제약으로 sub, nickname 중복 방지. Provider 사이에 sub 충돌 방지를 위해 (provider, sub)로 유니크 키 제약 필요할듯.
	}

}
