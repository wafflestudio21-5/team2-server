package com.wafflestudio.team2server.community.repository

import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<CommentEntity, Long> {
}
