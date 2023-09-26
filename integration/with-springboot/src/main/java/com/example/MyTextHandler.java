package com.example;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyTextHandler extends TextWebSocketHandler {

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) {
		System.out.println(message.getPayload());
	}
}
