package com.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class InboundHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		//获取客户端发送的消息内容
		String text = msg.text();
		System.out.println(InboundHandler.class + "收到客户端发送来的消息:  " + text);
		ctx.fireChannelRead(msg);
	}
}
