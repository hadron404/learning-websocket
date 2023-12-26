package com.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class WebsocketClient extends AbstractWebsocketClient {

	private static final NioEventLoopGroup NIO_GROUP = new NioEventLoopGroup();

	private final URI uri;

	private final int port;

	private Bootstrap bootstrap;

	private WebsocketClientHandler handler;

	private Channel channel;

	public WebsocketClient(String url) throws URISyntaxException, RuntimeException {
		super();
		this.uri = new URI(url);
		this.port = getPort();
	}

	/**
	 * Extract the specified port
	 *
	 * @return the specified port or the default port for the specific scheme
	 */
	private int getPort() throws RuntimeException {
		int port = uri.getPort();
		if (port == -1) {
			String scheme = uri.getScheme();
			if ("wss".equals(scheme)) {
				return 443;
			} else if ("ws".equals(scheme)) {
				return 80;
			} else {
				throw new RuntimeException("unknown scheme: " + scheme);
			}
		}
		return port;
	}

	@Override
	protected void doOpen() {
		// websocket客户端握手实现的基类
		WebSocketClientHandshaker webSocketClientHandshaker = WebSocketClientHandshakerFactory
			.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
		// 业务处理类
		handler = new WebsocketClientHandler(webSocketClientHandshaker);
		// client端，引导client channel启动
		bootstrap = new Bootstrap();
		// 添加管道 绑定端口 添加作用域等
		bootstrap.group(NIO_GROUP)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializerForWebsocketClient(handler));
	}

	@Override
	protected void doConnect() {
		try {
			// 启动连接
			channel = bootstrap.connect(uri.getHost(), port).sync().channel();
			// 等待握手响应
			handler.handshakeFuture().sync();
		} catch (InterruptedException e) {
			log.error("websocket连接发生异常", e);
			Thread.currentThread().interrupt();
		}
	}

	@Override
	protected Channel getChannel() {
		return channel;
	}

	@Override
	public void close() {
		if (channel != null) {
			channel.close();
		}
	}

	public boolean isOpen() {
		return channel.isOpen();
	}

}
