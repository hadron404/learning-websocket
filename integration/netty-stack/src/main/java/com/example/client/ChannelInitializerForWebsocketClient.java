package com.example.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;

class ChannelInitializerForWebsocketClient extends ChannelInitializer<SocketChannel> {
	private final WebsocketClientHandler handler;

	public ChannelInitializerForWebsocketClient(WebsocketClientHandler handler) {
		this.handler = handler;
	}

	@Override
	protected void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
		p
			.addLast(new HttpClientCodec())
			.addLast(new HttpObjectAggregator(8192))
			.addLast(WebSocketClientCompressionHandler.INSTANCE)
			.addLast(handler);
	}
}
