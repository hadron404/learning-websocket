package com.example.server;

import com.example.common.ApplicationContextUtils;
import com.example.server.handler.Sample1;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

class WebSocketServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final WebsocketServerProperties configProperties;

	WebSocketServerChannelInitializer() {
		this.configProperties = ApplicationContextUtils.getBean(WebsocketServerProperties.class);
	}

	@Override
	protected void initChannel(SocketChannel ch) {
//获取对应的管道
		ChannelPipeline pipeline = ch.pipeline();
		pipeline
			//添加HTTP编码解码器
			.addLast(new HttpServerCodec())
			//添加对大数据流的支持
			.addLast(new ChunkedWriteHandler())
			//添加聚合器
			.addLast(new HttpObjectAggregator(1024 * 64))
			.addLast(new IdleStateHandler(10, 5, -1))
			//设置websocket连接前缀前缀
			.addLast(new WebSocketServerProtocolHandler(this.configProperties.getWebsocketPath()))
			//添加自定义处理器 两个相同的handler类无法同时生效
			.addLast(new Sample1())
		;
	}

	int port() {
		return this.configProperties.getPort();
	}
}
