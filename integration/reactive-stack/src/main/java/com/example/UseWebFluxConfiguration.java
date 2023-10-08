package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class UseWebFluxConfiguration extends ApplicationObjectSupport {

	/**
	 * 手动配置 Websocket Endpoint
	 */
	@Bean
	public HandlerMapping basicHandlerMapping() {
		Map<String, WebSocketHandler> map = new HashMap<>();
		map.put("/inbound", new InboundHandler());
		map.put("/inout", new InboundAndOutboundHandler());
		int order = -1; // before annotated controllers
		return new SimpleUrlHandlerMapping(map, order);
	}

	/**
	 * 基于注解 {@link ServerEndPoint} 自动配置 Websocket Endpoint
	 */
	@Bean
	public HandlerMapping annotatedHandlerMapping() {
		return new SimpleUrlHandlerMapping(getAllWebSocketHandlers(), Ordered.HIGHEST_PRECEDENCE);
	}

	private Map<String, WebSocketHandler> getAllWebSocketHandlers() {
		final Map<String, WebSocketHandler> urlMap = new LinkedHashMap<>();
		Map<String, Object> beanMap = obtainApplicationContext().getBeansWithAnnotation(ServerEndPoint.class);
		beanMap.values()
			.forEach(bean -> {
				if (!(bean instanceof WebSocketHandler)) {
					throw new RuntimeException(
						String.format("Class [%s] doesn't implement WebSocketHandler interface.",
							bean.getClass().getName()));
				}
				ServerEndPoint annotation = AnnotationUtils.getAnnotation(bean.getClass(), ServerEndPoint.class);
				urlMap.put(Objects.requireNonNull(annotation).value(), (WebSocketHandler) bean);
			});
		return urlMap;
	}
}
