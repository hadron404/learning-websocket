package org.example;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * 语音转录后的显示
 * 客户端与服务端进行连接
 * 服务端会向客户端发送已转录好的语音识别结果文字
 */
@ServerEndpoint("/view")
public class ViewHandler implements WebSocketHandler {

	private final ClientManager clientManager;

	public ViewHandler(ClientManager clientManager) {
		this.clientManager = clientManager;
	}

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		// 1.收取消息
		// 2.缓存客户端连接，以便于转录完成后服务端通知
		// 3.移除客户端连接，在客户端断开时，从连接缓存中移除该客户端
		return session.receive()
			.doOnSubscribe(sub -> clientManager.connect(session))
			.doOnCancel(() -> clientManager.disConnect(session))
			.doOnTerminate(() -> clientManager.disConnect(session))
			.then();
	}
}
