package org.example;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalTime;

class SSEApplicationTest {

	/**
	 * 测试结果看不出效果
	 */
	@Test
	void consumeServerSentEvent() {
		WebClient client = WebClient.create("http://localhost:8080");
		ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {
		};
		Flux<ServerSentEvent<String>> eventStream = client.get()
			.uri("/stream-sse")
			.retrieve()
			.bodyToFlux(type);

		eventStream
			.subscribe(
				content -> System.out.printf("Time: {%s} - event: name[{%s}], id [{%s}], content[{%s}] %n",
					LocalTime.now(), content.event(), content.id(), content.data()),
				error -> System.out.print("Error receiving SSE"),
				() -> System.out.print("Completed!!!"));
	}
}
