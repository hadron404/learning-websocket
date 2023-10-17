package org.example;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClientManager {
	private static final ConcurrentHashMap<STTChannelId, WebSocketSession> CLIENTS_VIEW = new ConcurrentHashMap<>();

	private static final ConcurrentHashMap<STTChannelId, WebSocketSession> CLIENTS_TS = new ConcurrentHashMap<>();

	public void connectTs(WebSocketSession session) {
		CLIENTS_TS.put(getChannelIdByHandshakeInfo(session.getHandshakeInfo()), session);
	}

	public WebSocketSession getTsSession(WebSocketSession session) {
		return CLIENTS_TS.get(ClientManager.getChannelIdByHandshakeInfo(session.getHandshakeInfo()));
	}

	public void disconnectTs(WebSocketSession session) {
		CLIENTS_TS.remove(ClientManager.getChannelIdByHandshakeInfo(session.getHandshakeInfo()));
	}

	public void connect(WebSocketSession session) {
		STTChannelId STTChannelId = getChannelIdByHandshakeInfo(session.getHandshakeInfo());
		CLIENTS_VIEW.put(STTChannelId, session);
	}

	public void disConnect(WebSocketSession session) {
		STTChannelId STTChannelId = getChannelIdByHandshakeInfo(session.getHandshakeInfo());
		CLIENTS_VIEW.remove(STTChannelId);
	}


	public Optional<WebSocketSession> getSession(HandshakeInfo handshakeInfo) {
		STTChannelId STTChannelId = getChannelIdByHandshakeInfo(handshakeInfo);
		return Optional.ofNullable(CLIENTS_VIEW.get(STTChannelId));
	}

	private static String getChannelId(HandshakeInfo handshakeInfo) {
		UriComponents uri = UriComponentsBuilder.fromUri(handshakeInfo.getUri()).build();
		Map<String, String> queryMap = uri.getQueryParams().toSingleValueMap();
		return queryMap.getOrDefault("channelId", UUID.randomUUID().toString());
	}

	/**
	 * 根据连接握手信息，获取 clientId，若握手信息中无 clientId，则生成一个全局唯一的 clientId
	 *
	 * @return 一个全局唯一的 clientId
	 */
	public static STTChannelId getChannelIdByHandshakeInfo(HandshakeInfo handshakeInfo) {
		return new STTChannelId(getChannelId(handshakeInfo));
	}

	record STTChannelId(String value) {
	}
}
