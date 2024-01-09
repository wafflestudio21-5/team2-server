package com.wafflestudio.team2server.storage.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.storage.service.StorageService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@Controller
class StorageController(private val storageService: StorageService) {
	@PostMapping("/storage/upload")
	fun uploadFile(
		multipartFiles: List<MultipartFile>,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
	): List<String> {
		return storageService.uploadFile(multipartFiles)
	}

	@DeleteMapping("/storage/delete")
	fun deleteFile(
		@RequestParam fileName: String,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
	) {
		storageService.deleteFile(fileName)
	}
}
