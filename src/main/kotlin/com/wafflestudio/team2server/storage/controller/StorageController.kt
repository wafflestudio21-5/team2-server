package com.wafflestudio.team2server.storage.controller

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.storage.service.StorageService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/image")
class StorageController(private val storageService: StorageService) {
	@PostMapping("/upload")
	fun uploadFile(
		multipartFiles: List<MultipartFile>,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
	): ImageResponse {
		return ImageResponse(storageService.uploadFile(multipartFiles))
	}

	@DeleteMapping("/delete")
	fun deleteFile(
		@RequestParam fileName: String,
		@AuthenticationPrincipal authUserInfo: AuthUserInfo,
	) {
		storageService.deleteFile(fileName)
	}

	data class ImageResponse(
		val images: List<String>,
	)
}
