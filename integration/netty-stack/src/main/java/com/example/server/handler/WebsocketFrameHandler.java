package com.example.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.web.socket.PingMessage;

@ChannelHandler.Sharable//保证处理器，在整个生命周期中就是以单例的形式存在
public class WebsocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
		if (frame instanceof TextWebSocketFrame textFrame) {
			// Handle text messages
			String text = textFrame.text();
			// Process the text message and send a response, if necessary
		} else if (frame instanceof BinaryWebSocketFrame binaryFrame) {
			// Handle binary messages
			ByteBuf content = binaryFrame.content();
			// Process the binary data and send a response, if necessary
		} else if (frame instanceof PingWebSocketFrame pingFrame) {
			// Handle ping messages
		} else if (frame instanceof PongWebSocketFrame pongFrame) {
			// Handle pong messages
		} else if (frame instanceof CloseWebSocketFrame closeFrame) {
			// Handle close messages
		} else {
			// Other frame types an be handled here
		}
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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.out.println("捕捉到异常：" + cause.getMessage());
		ctx.close();
	}
}
