package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class UseWebFluxConfiguration {

	@Bean
	public HandlerMapping handlerMapping() {
		Map<String, WebSocketHandler> map = new HashMap<>();
		map.put("/path", new InboundAndOutboundHandler());
		int order = -1; // before annotated controllers
		return new SimpleUrlHandlerMapping(map, order);
	}
}
