package com.example;


import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;


/**
 *
 */
// @ServerEndPoint annotation to specify that a class is used as a WebSocket server endpoint
@ServerEndpoint(value = "/echo")
public class NativeEndpoint {

	@OnMessage
	public String onMessage(String message, Session session) {
		return message;
	}

}
