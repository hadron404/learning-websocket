package org.example;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * 实时语音转文字
 * 客户端向服务端发送一段语音，
 * 服务端向客户端发送转录后文本
 */
@ServerEndPoint("/recognize")
public class RecognizeHandler implements WebSocketHandler {
	@Override
	public Mono<Void> handle(WebSocketSession session) {
		return null;
	}
}
