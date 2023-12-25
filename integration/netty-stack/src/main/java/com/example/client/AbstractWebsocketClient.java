package com.example.client;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.io.Closeable;

public abstract class AbstractWebsocketClient implements Closeable {

	/**
	 * 发送消息.<br>
	 *
	 * @param message 发送文本
	 */
	public void send(String message) throws RuntimeException {
		Channel channel = getChannel();
		if (channel != null) {
			channel.writeAndFlush(new TextWebSocketFrame(message));
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

