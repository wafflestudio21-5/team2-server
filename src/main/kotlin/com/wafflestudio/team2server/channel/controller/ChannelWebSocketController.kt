package com.wafflestudio.team2server.channel.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.RestController

@RestController
class ChannelWebSocketController(
	private val simpMessagingTemplate: SimpMessagingTemplate,
	private val objectMapper: ObjectMapper,
) {

}
