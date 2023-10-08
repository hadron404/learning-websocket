package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 使用spring封装的websocket实现的配置
 */
@Configuration
@EnableWebSocket
public class WebSocketConfigurationForSpring implements WebSocketConfigurer {

	@Bean
	public WebSocketHandler myTextHandler() {
		return new UsingSpring();
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry
			.addHandler(myTextHandler(), "/text")
			.setAllowedOrigins("*")
		;
	}
}
