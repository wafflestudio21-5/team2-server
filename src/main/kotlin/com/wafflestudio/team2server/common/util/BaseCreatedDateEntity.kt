package com.wafflestudio.team2server.common.util

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseCreatedDateEntity(
	@CreatedDate
	@Column(nullable = false, updatable = false)
	var createdAt: Instant? = null
)
