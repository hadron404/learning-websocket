package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@RestController
@CrossOrigin
public class SSEApplication {
	public static void main(String[] args) {
		SpringApplication.run(SSEApplication.class);
	}

	/**
	 * Stream Events Using Flux
	 */
	@GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> streamFlux() {
		return Flux.interval(Duration.ofSeconds(1))
			.map(sequence -> "Flux - " + LocalTime.now().toString());
	}


	/**
	 * Making use of the ServerSentEvent Element
	 * */
	@GetMapping("/stream-sse")
	public Flux<ServerSentEvent<String>> streamEvents() {
		return Flux.interval(Duration.ofSeconds(1))
			.map(sequence -> ServerSentEvent.<String> builder()
				.id(String.valueOf(sequence))
				.event("periodic-event")
				.data("SSE - " + LocalTime.now().toString())
				.build());
	}
	private final ExecutorService nonBlockingService = Executors
		.newCachedThreadPool();
	@GetMapping("/stream-sse-mvc")
	public SseEmitter streamSseMvc() {
		SseEmitter emitter = new SseEmitter();
		nonBlockingService.execute(() -> {
			try {
				for (int i = 0; true; i++) {
					SseEmitter.SseEventBuilder event = SseEmitter.event()
						.data("SSE MVC - " + LocalTime.now())
						.id(String.valueOf(i))
						.name("pong");
					emitter.send(event);
					Thread.sleep(1000);
				}
			} catch (Exception ex) {
				emitter.completeWithError(ex);
			}
		});
		return emitter;
	}
}
