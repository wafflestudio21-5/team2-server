package com.wafflestudio.team2server.storage.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class StorageConfig {
	@Value("\${cloud.aws.credentials.accessKey}")
	private val accessKey: String? = null

	@Value("\${cloud.aws.credentials.secretKey}")
	private val accessSecret: String? = null

	@Value("\${cloud.aws.region.static}")
	private val region: String? = null

	@Bean
	fun s3Client(): AmazonS3 {
		val credentials = BasicAWSCredentials(accessKey, accessSecret)
		return AmazonS3ClientBuilder.standard()
			.withCredentials(AWSStaticCredentialsProvider(credentials))
			.withRegion(region).build()
	}
}
