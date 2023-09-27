package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;

@Configuration
public class UseAnnotationAutoRegistryConfiguration {
	@Bean
	public HandlerMapping webSocketMapping() {
		return new AnnotatedUrlHandlerMapping();
	}

}
