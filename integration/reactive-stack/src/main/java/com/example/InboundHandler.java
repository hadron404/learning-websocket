package com.example;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@WSRequestMapping("/text")// if use auto-registry configuration use this annotation
public class InboundHandler implements WebSocketHandler {

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		// access the stream of inbound messages
		return session.receive()
			// do sth with each message
			.doOnNext(m -> System.out.println(m.getPayloadAsText()))
			// return a Mono<Void> that completes when receiving completes
			.then();
	}
}
