package com.example.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class WebsocketClientTest {
//| RuntimeException | InterruptedException e

	@Test
	void name() {
		Assertions.assertDoesNotThrow(() -> {
			try (DefaultWebsocketClient websocketClient =
					 new DefaultWebsocketClient("ws://localhost:12424/netty")) {
				// 连接
				websocketClient.connect();
				// 发送消息
//				websocketClient.send("xxxxxxxxxxxxxxxxx");
				while (true) {
					// 阻塞一下，否则这里客户端会调用close方法
					Thread.sleep(1000);
				}
			}
		});
	}
}
