package com.example.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * 集群中主动向所有客户端发送消息
 */
@Slf4j
public class Sample2InCluster extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private static final ChannelGroup CLIENTS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
		log.info("收到客户端发送来的消息: {} ", msg.text());
		//遍历出所有连接的通道
		for (Channel channel : CLIENTS) {
			//推送给所有的通道
			channel.writeAndFlush(new TextWebSocketFrame("服务器: 收到客户端发送来的消息: " + msg.text()));
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		//加入通道组
		CLIENTS.add(ctx.channel());
	}

	/**
	 * 不活跃时会调用这个方法
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		//移除出通道组
		CLIENTS.remove(ctx.channel());
	}


}
