package com.example;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * implementation combines the inbound and outbound streams
 * */
public class InboundAndOutboundHandler implements WebSocketHandler {
	@Override
	public Mono<Void> handle(WebSocketSession session) {

		Flux<WebSocketMessage> output = session.receive()
			// handle the inbound message stream
			.doOnNext(message -> System.out.println(message.getPayloadAsText()))
			// create the outbound message,producing a combined stream
			.map(value -> session.textMessage("Echo " + value));
		// return a Mono<Void> that does not complete while we continue to receive
		return session.send(output);
	}
}
