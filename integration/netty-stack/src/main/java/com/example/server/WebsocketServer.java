package com.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class WebsocketServer {

	/**
	 * Netty服务器启动对象
	 */
	private final ServerBootstrap serverBootstrap;
	private final WebSocketServerChannelInitializer websocketChannelInitializer;


	public WebsocketServer() {
		this.websocketChannelInitializer = new WebSocketServerChannelInitializer();
		// 初始化服务器启动对象
		this.serverBootstrap = new ServerBootstrap();
	}

	public void doStart() throws InterruptedException {
		log.info("Websocket服务端启动中，正在监听：{}", this.websocketChannelInitializer.port());
		// 主线程池
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		// 从线程池
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		this.serverBootstrap
			// 指定使用上面创建的两个线程池
			.group(bossGroup, workerGroup)
			.localAddress(this.websocketChannelInitializer.port())
			// 指定Netty通道类型
			.channel(NioServerSocketChannel.class)
			// 指定通道初始化器用来加载当Channel收到事件消息后
			.childHandler(this.websocketChannelInitializer)
		;
		// 绑定服务器端口，以异步的方式启动服务器
		serverBootstrap.bind().sync();
	}

}
