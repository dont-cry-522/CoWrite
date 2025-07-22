package com.cowrite.project.netty.server;

import com.cowrite.project.netty.handler.ServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 服务器初始化器
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 初始化通道
     * @param ch 通道
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        // 添加HTTP编解码器
        ch.pipeline().addLast(new HttpServerCodec());
        // 添加HTTP消息聚合器
        ch.pipeline().addLast(new HttpObjectAggregator(65536));
        // 添加WebSocket协议处理器
        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
        // 添加心跳检测
        ch.pipeline().addLast(new IdleStateHandler(300, 0, 0, TimeUnit.SECONDS));
        // 添加自定义处理器
        ch.pipeline().addLast(new ServerHandler());
    }
}