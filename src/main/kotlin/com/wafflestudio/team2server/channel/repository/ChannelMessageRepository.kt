package com.wafflestudio.team2server.channel.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChannelMessageRepository: JpaRepository<ChannelMessageEntity, Long> {
}
