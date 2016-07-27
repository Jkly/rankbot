package jkly.rankbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor

@SpringBootApplication
@EnableWebSocket
open class RankbotWebApp : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(slackHandler(), "/slack").addInterceptors(HttpSessionHandshakeInterceptor())
    }

    @Bean
    open fun slackHandler() = SlackHandler()
}