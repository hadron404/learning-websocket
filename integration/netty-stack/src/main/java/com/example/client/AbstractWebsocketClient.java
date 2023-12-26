package com.example.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.io.Closeable;

abstract class AbstractWebsocketClient implements Closeable {

	/**
	 * 发送消息.<br>
	 *
	 * @param message 发送文本
	 */
	public void sendText(String message) throws RuntimeException {
		this.send(new TextWebSocketFrame(message));
	}

	public void sendBinary(ByteBuf byteBuf) {
		this.send(new BinaryWebSocketFrame(byteBuf.retain()));
	}

	public void send(WebSocketFrame frame) throws RuntimeException {
		Channel channel = getChannel();
		if (channel != null) {
			channel.writeAndFlush(frame);
			return;
		}
		throw new RuntimeException("连接已经关闭");
	}

	/**
	 * 连接并发送消息.<br>
	 */
	public void connect() throws RuntimeException {
		try {
			doOpen();
			doConnect();
		} catch (Exception e) {
			throw new RuntimeException("连接没有成功打开,原因是:{}" + e.getMessage(), e);
		}
	}

	/**
	 * 初始化连接.<br>
	 */
	protected abstract void doOpen();

	/**
	 * 建立连接.<br>
	 */
	protected abstract void doConnect() throws RuntimeException;

	/**
	 * 获取本次连接channel.<br>
	 *
	 * @return {@link Channel}
	 */
	protected abstract Channel getChannel();

	/**
	 * 关闭连接.<br>
	 */
	@Override
	public abstract void close();

}

