package org.example;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * 客户端向服务端发送语音消息（处理前语音（二进制、音频文件等格式）
 * 客户端发送的语音消息处理（语音内容处理）
 * 服务端将语音消息送至sst服务进行识别（处理后语音）
 */
@ServerEndpoint("/speech")
public class SpeechHandler implements WebSocketHandler {
	private final ClientManager clientManager;

	private final TranscriptService transcriptService;

	public SpeechHandler(ClientManager clientManager, TranscriptService transcriptService) {
		this.clientManager = clientManager;
		this.transcriptService = transcriptService;
	}

	@Override()
	public Mono<Void> handle(WebSocketSession session) {
		return session.receive()
			// 通话建立时同时建立转录服务的ws连接
			.doOnSubscribe((sub) -> transcriptService.establish(session))
			.doOnNext(m -> System.out.println(m.getPayloadAsText()))
			.flatMap(message -> {
				WebSocketSession tsSession = clientManager.getTsSession(session);
				if (tsSession != null) {
					return tsSession.send(Mono.just(tsSession.textMessage(message.getPayloadAsText())));
				}
				return session.send(Mono.just(session.textMessage("转录服务不可用")));
			})
			.doOnTerminate(() -> clientManager.disconnectTs(session))
			.then();
	}

}
