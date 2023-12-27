package com.example.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


// 了解 ApplicationListener 的作用
@Slf4j
@Component
class ServerStarter implements ApplicationListener<ContextRefreshedEvent> {

	private final WebsocketServer websocketServer;

	public ServerStarter() {
		this.websocketServer = new WebsocketServer();
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//判断event上下文中的父级是否为空
		if (event.getApplicationContext().getParent() == null) {
			try {
				//为空则调用start方法
				this.websocketServer.doStart();
			} catch (Exception e) {
				log.error("服务端启动失败", e);
			}
		}
	}
}
