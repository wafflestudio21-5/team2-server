package com.wafflestudio.team2server.storage.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.wafflestudio.team2server.common.error.BaniException
import com.wafflestudio.team2server.common.error.ErrorType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*


@Service
class StorageService(
	@Autowired
	private val amazonS3: AmazonS3
) {
	@Value("\${cloud.aws.s3.bucket}")
	private val bucket: String? = null

	fun uploadFile(multipartFiles: List<MultipartFile>): List<String> {
		val fileNameList: MutableList<String> = mutableListOf()
		multipartFiles.forEach { file ->
			val fileName = createFileName(file.originalFilename ?: "")
			val objectMetadata = ObjectMetadata()
			objectMetadata.contentLength = file.size
			objectMetadata.contentType = file.contentType
			val inputStream = file.inputStream
			amazonS3.putObject(
				PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
					.withCannedAcl(CannedAccessControlList.PublicRead)
			)
			fileNameList.add(fileName);
		}
		return fileNameList
	}

	fun getFileExtension(fileName: String): String {
		val ext = try {
			fileName.substring(fileName.lastIndexOf("."))
		} catch (e: StringIndexOutOfBoundsException) {
			throw BaniException(ErrorType.BAD_FILE_FORMAT)
		}
		if (ext == ".jpg" || ext == ".jpeg" || ext == ".png") { // 업로드 가능 확장자
			return ext
		}
		throw BaniException(ErrorType.BAD_FILE_FORMAT)
	}

	fun createFileName(fileName: String): String {
		return UUID.randomUUID().toString() + getFileExtension(fileName)
	}

	fun deleteFile(fileName: String?) {
		amazonS3.deleteObject(DeleteObjectRequest(bucket, fileName))
		println(bucket)
	}
}
