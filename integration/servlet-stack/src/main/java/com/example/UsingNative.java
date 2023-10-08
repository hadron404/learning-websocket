package com.example;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@Component
@ServerEndpoint("/native")
public class UsingNative {

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session) {
		// 	do something
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		// 	do something

	}

	/**
	 * @param session 会话
	 * @param error   异常
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		// 	do something
	}

}
