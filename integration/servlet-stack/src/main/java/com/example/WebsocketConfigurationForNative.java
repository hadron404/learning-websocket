package com.example;

import jakarta.websocket.server.ServerEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 使用spring整合Jakarta EE websocket方式的配置
 */
@Configuration
public class WebsocketConfigurationForNative {
	/**
	 * ServerEndpointExporter 作用
	 * <p>
	 * 这个Bean会自动注册使用 {@link ServerEndpoint} 注解声明的websocket endpoint（必须是spring容器管理的）
	 */
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
