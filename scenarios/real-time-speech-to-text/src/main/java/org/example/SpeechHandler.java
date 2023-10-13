package org.example;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * 客户端发送的语音消息处理
 * 客户端向服务端发送语音消息
 * 服务端将语音消息送至sst服务进行识别
 */
@ServerEndPoint("/speech")
public class SpeechHandler implements WebSocketHandler {
	@Override
	public Mono<Void> handle(WebSocketSession session) {
		return null;
	}
}
