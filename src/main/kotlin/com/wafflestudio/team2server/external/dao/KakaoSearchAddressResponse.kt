package com.wafflestudio.team2server.external.dao

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoSearchAddressResponse(
	val meta: LocalMeta,
	val documents: List<ComplexAddress>
)

data class LocalMeta(
	@JsonProperty("total_count")
	val totalCount: Int,
	@JsonProperty("pageable_count")
	val pageableCount: Int,
	@JsonProperty("is_end")
	val isEnd: Boolean,
)

data class ComplexAddress(
	@JsonProperty("address_name")
	val addressName: String,
	val address: Address,
)

data class Address(
	@JsonProperty("address_name")
	val addressName: String,
	@JsonProperty("region_1depth_name")
	val region1DepthName: String,
	@JsonProperty("region_2depth_name")
	val region2DepthName: String,
	@JsonProperty("region_3depth_name")
	val region3DepthName: String,
	@JsonProperty("region_3depth_h_name")
	val region3DepthHName: String,
	@JsonProperty("h_code")
	val hCode: String,
) {
	fun isHArea(): Boolean {
		val hCodeNum = hCode.toLongOrNull()
		if (hCodeNum == null || hCodeNum % 100L != 0L || region3DepthHName.isBlank()) {
			return false
		}
		return true
	}
}
