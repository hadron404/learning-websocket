package com.example.startup.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "netty")
public class NettyConfigProperties {

	/**
	 * Websocket服务端口
	 */
	private int port;
	/**
	 * boss线程数量 默认为cpu线程数*4
	 */
	int bossThread;
	/**
	 * worker线程数量 默认为cpu线程数*2
	 */
	int workerThread;
	boolean keepalive;
	/**
	 * 当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
	 */
	int backlog;
	/**
	 * URI路径
	 */
	String websocketPath;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getBossThread() {
		return bossThread;
	}

	public void setBossThread(int bossThread) {
		this.bossThread = bossThread;
	}

	public int getWorkerThread() {
		return workerThread;
	}

	public void setWorkerThread(int workerThread) {
		this.workerThread = workerThread;
	}

	public boolean isKeepalive() {
		return keepalive;
	}

	public void setKeepalive(boolean keepalive) {
		this.keepalive = keepalive;
	}

	public int getBacklog() {
		return backlog;
	}

	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	public String getWebsocketPath() {
		return websocketPath;
	}

	public void setWebsocketPath(String websocketPath) {
		this.websocketPath = websocketPath;
	}
}
