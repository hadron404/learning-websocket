package com.example;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@ServerEndPoint("/anno")
public class AnnotatedHandler implements WebSocketHandler {

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		return session.receive()
			.doOnNext(m -> System.out.println(m.getPayloadAsText()))
			.then();
	}
}
