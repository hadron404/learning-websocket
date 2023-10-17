package org.example;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Optional;

@Service
public class TranscriptService {

	private final ClientManager clientManager;

	public TranscriptService(ClientManager clientManager) {
		this.clientManager = clientManager;
	}

	public void establish(WebSocketSession session) {
		WebSocketClient client = new ReactorNettyWebSocketClient(
			// 20s 后未收到响应，自动断开
			HttpClient.create().responseTimeout(Duration.ofSeconds(20))
		);
		//  第三方语音文本识别服务 ws地址
		String path = "ws://127.0.0.1:8080/recognize";
		UriComponents URL = UriComponentsBuilder.fromUriString(path)
			.queryParam("channelId", ClientManager.getChannelIdByHandshakeInfo(session.getHandshakeInfo()).value())
			.build();
		client.execute(
				URL.toUri(),
				tsSession -> tsSession.receive()
					.doOnSubscribe(sub -> clientManager.connectTs(tsSession))
					.doOnNext((message) -> System.out.println(message.getPayloadAsText()))
					.flatMap(message -> {
						Optional<WebSocketSession> viewSession = clientManager.getSession(tsSession.getHandshakeInfo());
						if (viewSession.isPresent()) {
							WebSocketSession target = viewSession.get();
							return target.send(Mono.just(target.textMessage(message.getPayloadAsText())));
						}
						return session.send(Mono.just(session.textMessage("目标用户不在线")));
					})
					.then()
			)
			.subscribe();
	}
}
