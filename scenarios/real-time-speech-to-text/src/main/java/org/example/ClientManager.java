package org.example;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClientManager {
	private static final ConcurrentHashMap<ClientId, Client> CLIENTS = new ConcurrentHashMap<>();


	public ClientId connect(WebSocketSession session) {
		ClientId clientId = new ClientId(session.getId());
		CLIENTS.put(clientId, new Client(session));
		return clientId;
	}

	public WebSocketMessage connectAsMessage(WebSocketSession session) {
		return session.textMessage(connect(session).value);
	}

	public void disConnect(WebSocketSession session) {
		ClientId clientId = new ClientId(session.getId());
		CLIENTS.remove(clientId);
	}


	/**
	 * 根据连接握手信息，获取 clientId，若握手信息中无 clientId，则生成一个全局唯一的 clientId
	 *
	 * @return 一个全局唯一的 clientId
	 */
	private static ClientId getClientIdByHandshakeInfo(HandshakeInfo handshakeInfo) {
		UriComponents uri = UriComponentsBuilder.fromUri(handshakeInfo.getUri()).build();
		Map<String, String> queryMap = uri.getQueryParams().toSingleValueMap();
		String id = queryMap.getOrDefault("clientId", UUID.randomUUID().toString());
		return new ClientId(id);
	}

	static class Client {
		private final WebSocketSession session;
		// private final FluxSink<WebSocketMessage> sink;

		public Client(WebSocketSession session) {
			this.session = session;
			// this.sink = sink;
		}

		public void sendData(String data) {
			// sink.next(session.textMessage(data));
		}
	}

	record ClientId(String value) {
	}
}
