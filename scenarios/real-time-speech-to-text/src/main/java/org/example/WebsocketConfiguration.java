package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class WebsocketConfiguration extends ApplicationObjectSupport {


	/**
	 * 基于注解 {@link ServerEndpoint} 自动配置 Websocket Endpoint
	 */
	@Bean
	public HandlerMapping annotatedHandlerMapping() {
		return new SimpleUrlHandlerMapping(getAllWebSocketHandlers(), Ordered.HIGHEST_PRECEDENCE);
	}

	private Map<String, WebSocketHandler> getAllWebSocketHandlers() {
		final Map<String, WebSocketHandler> urlMap = new LinkedHashMap<>();
		Map<String, Object> beanMap = obtainApplicationContext().getBeansWithAnnotation(ServerEndpoint.class);
		beanMap.values()
			.forEach(bean -> {
				if (!(bean instanceof WebSocketHandler)) {
					throw new RuntimeException(
						String.format("Class [%s] doesn't implement WebSocketHandler interface.",
							bean.getClass().getName()));
				}
				ServerEndpoint annotation = AnnotationUtils.getAnnotation(bean.getClass(), ServerEndpoint.class);
				urlMap.put(Objects.requireNonNull(annotation).value(), (WebSocketHandler) bean);
			});
		return urlMap;
	}
}
