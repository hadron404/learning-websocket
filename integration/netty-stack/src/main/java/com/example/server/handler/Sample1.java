package com.example.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端服务端进行单独通信
 * 场景一：客户端向服务端发送一条消息，服务端随之发送响应消息
 */
@Slf4j
public class Sample1 extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
		//获取客户端发送的消息内容
		log.info("收到来自客户端的文本消息： {} ", msg);
		msg.content().retain();
		// 如果有需要，可以通过以下代码将消息发送到下一个handler，按照Initializer注册顺序
//		ctx.fireChannelRead(msg);
		ctx.channel().writeAndFlush(new TextWebSocketFrame("服务端响应消息"));
	}
}
