package com.example.server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "netty")
class WebsocketServerProperties {

	/**
	 * Websocket服务端口
	 */
	private int port;
	/**
	 * boss线程数量 默认为cpu线程数*4
	 */
	private int bossThread;
	/**
	 * worker线程数量 默认为cpu线程数*2
	 */
	private int workerThread;
	private boolean keepalive;
	/**
	 * 当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
	 */
	private int backlog;
	/**
	 * URI路径
	 */
	private String websocketPath;

}
