package com.wafflestudio.team2server.external

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class KakaoApi(
	@Value("\${kakao.api-key}") private val apiKey: String,
) {

	private val httpClient = HttpClient.newBuilder()
		.build()

	fun searchAddress(query: String, cursor: Int) {
		val request = HttpRequest.newBuilder(URI("https://dapi.kakao.com/v2/local/search/address?query=$query&page=$cursor"))
			.setHeader("authorization", "KakaoAK $apiKey")
			.setHeader("content-type", "application/json;charset=UTF-8")
			.build()
		val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
		println(response.body())
	}

}
