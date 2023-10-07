package com.example.startup;

import com.example.InboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

@Component
public class NettyServer {

	private final NettyConfigProperties nettyConfig;
	/**
	 * Netty服务器启动对象
	 */
	private final ServerBootstrap serverBootstrap;
	private final NettyChannelInitializerForWebSocket websocketChannelInitializer;


	public NettyServer(NettyConfigProperties nettyConfig,
		NettyChannelInitializerForWebSocket websocketChannelInitializer) {
		this.nettyConfig = nettyConfig;
		this.websocketChannelInitializer = websocketChannelInitializer;
		// 初始化服务器启动对象
		this.serverBootstrap = new ServerBootstrap();
	}

	public void start() throws InterruptedException {
		System.out.println(NettyServer.class + " 启动正在监听：" + nettyConfig.getPort());
		// 主线程池
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		// 从线程池
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		this.serverBootstrap
			// 指定使用上面创建的两个线程池
			.group(bossGroup, workerGroup)
			.localAddress(this.nettyConfig.getPort())
			// 指定Netty通道类型
			.channel(NioServerSocketChannel.class)
			// 指定通道初始化器用来加载当Channel收到事件消息后
			.childHandler(this.websocketChannelInitializer)
		;
		// 绑定服务器端口，以异步的方式启动服务器
		serverBootstrap.bind().sync();
	}

}
