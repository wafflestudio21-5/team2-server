package com.wafflestudio.team2server.config

import com.wafflestudio.team2server.channel.service.ChannelHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.*

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
	private val channelHandler: ChannelHandler,
) : WebSocketConfigurer{
	override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
		registry.addHandler(channelHandler, "/channel").setAllowedOrigins("*")
	}

}
