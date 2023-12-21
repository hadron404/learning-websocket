package com.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.web.socket.PingMessage;

public class InboundHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
		//获取客户端发送的消息内容
		System.out.println(InboundHandler.class + "收到客户端发送来的消息:  " + msg);
		msg.content().retain();
		// 将消息发送到下一个handler，按照Initializer注册顺序
		ctx.fireChannelRead(msg);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
		if (evt instanceof IdleStateEvent e) {
			if (e.state() == IdleState.READER_IDLE) {
				ctx.close();
			} else if (e.state() == IdleState.WRITER_IDLE) {
				ctx.writeAndFlush(new PingMessage());
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("捕捉到异常：" + cause.getMessage());
		ctx.close();
	}
}
