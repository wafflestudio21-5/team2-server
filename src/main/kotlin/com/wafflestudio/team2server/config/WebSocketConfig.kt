package com.wafflestudio.team2server.config

import com.wafflestudio.team2server.channel.service.ChannelDetailHandler
import com.wafflestudio.team2server.channel.service.UserChannelHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.*

@Configuration
@EnableWebSocket
class WebSocketConfig(
	private val channelDetailHandler: ChannelDetailHandler,
	private val userChannelHandler: UserChannelHandler,
) : WebSocketConfigurer{
	override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
		registry.addHandler(channelDetailHandler, "/ws/channels/{channelId}").setAllowedOriginPatterns("*")
		registry.addHandler(userChannelHandler, "/ws/users").setAllowedOriginPatterns("*")
	}
}
