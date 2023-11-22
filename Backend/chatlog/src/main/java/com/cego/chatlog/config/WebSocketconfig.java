package com.cego.chatlog.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.cego.chatlog.controller.websocket.serverWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketconfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/websocket")
        .setAllowedOrigins("http://localhost:3000");
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new serverWebSocketHandler();
    }
}