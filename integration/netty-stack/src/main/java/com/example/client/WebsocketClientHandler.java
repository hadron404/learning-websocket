package com.example.client;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;


@Slf4j
class WebsocketClientHandler extends SimpleChannelInboundHandler<Object> {
	/**
	 * 连接处理器
	 */
	private final WebSocketClientHandshaker webSocketClientHandshaker;

	public WebsocketClientHandler(WebSocketClientHandshaker webSocketClientHandshaker) {
		this.webSocketClientHandshaker = webSocketClientHandshaker;
	}

	/**
	 * netty提供的数据过程中的数据保证
	 */
	private ChannelPromise handshakeFuture;

	private Channel channel;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
		if (!webSocketClientHandshaker.isHandshakeComplete()) {
			this.handleHttpRequest(msg);
			log.info("websocket已经建立连接");
			return;
		}
		if (msg instanceof FullHttpResponse response) {
			throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
		}
		this.handleWebSocketFrame(msg);
	}

	public ChannelFuture handshakeFuture() {
		return handshakeFuture;
	}


	/**
	 * ChannelHandler添加到实际上下文中准备处理事件,调用此方法
	 *
	 * @param ctx ChannelHandlerContext
	 */

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		handshakeFuture = ctx.newPromise();
	}

	/**
	 * 当客户端主动链接服务端的链接后,调用此方法
	 *
	 * @param ctx ChannelHandlerContext
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		channel = ctx.channel();
		webSocketClientHandshaker.handshake(channel);
		log.info("channelActive");
	}

	/**
	 * 链接断开后,调用此方法
	 *
	 * @param ctx ChannelHandlerContext
	 */
	@Override
	public void channelInactive(@Nonnull ChannelHandlerContext ctx) {
		log.info("channelInactive");
	}

	/**
	 * 处理http连接请求.<br>
	 *
	 * @param msg:
	 */
	private void handleHttpRequest(Object msg) {
		webSocketClientHandshaker.finishHandshake(channel, (FullHttpResponse) msg);
		handshakeFuture.setSuccess();
	}

	/**
	 * 处理文本帧请求.<br>
	 *
	 * @param msg 消息
	 */
	private void handleWebSocketFrame(Object msg) {
		WebSocketFrame frame = (WebSocketFrame) msg;
		if (frame instanceof TextWebSocketFrame textFrame) {
			// ...自定义
			log.info("收到消息：{}", textFrame.text());
		} else if (frame instanceof CloseWebSocketFrame) {
			log.info("连接收到关闭帧");
			channel.close();
		} else if (frame instanceof PingWebSocketFrame) {
			log.info("连接收到ping帧");
//			对于某些websocket服务，会有心跳机制，在一定时间未收到读写请求，会断开websocket连接
			channel.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
		}
	}

	/**
	 * 运行过程中未捕获的异常,调用此方法
	 *
	 * @param ctx   ChannelHandlerContext
	 * @param cause Throwable
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.info("监控触发异常=>{}", cause.getMessage(), cause);
	}
}
