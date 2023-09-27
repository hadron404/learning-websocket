package com.example;

import org.springframework.beans.BeansException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 将带有 WebSocketMapping 注解的 handler实现类自动注入到容器中
 * 无需手动配置，即无需进行 UseWebFluxConfiguration 相关配置
 */
public class AnnotatedUrlHandlerMapping extends SimpleUrlHandlerMapping {

	private final Map<String, WebSocketHandler> handlerMap = new LinkedHashMap<>();


	@Override
	public void initApplicationContext() throws BeansException {
		Map<String, Object> beanMap = obtainApplicationContext().getBeansWithAnnotation(WSRequestMapping.class);
		beanMap.values().forEach(bean -> {
			if (!(bean instanceof WebSocketHandler)) {
				throw new RuntimeException(
					String.format("Controller [%s] doesn't implement WebSocketHandler interface.",
						bean.getClass().getName()));
			}
			WSRequestMapping annotation = AnnotationUtils.getAnnotation(bean.getClass(), WSRequestMapping.class);
			handlerMap.put(Objects.requireNonNull(annotation).value(), (WebSocketHandler) bean);
		});
		super.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.setUrlMap(handlerMap);
		super.initApplicationContext();
	}
}
