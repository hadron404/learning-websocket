package com.example.startup.server;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
// 了解 ApplicationListener 的作用
public class NettyStartListener implements ApplicationListener<ContextRefreshedEvent> {

	private final NettyServer nettyServer;

	public NettyStartListener(NettyServer nettyServer) {
		this.nettyServer = nettyServer;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//判断event上下文中的父级是否为空
		if (event.getApplicationContext().getParent() == null) {
			try {
				//为空则调用start方法
				this.nettyServer.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
