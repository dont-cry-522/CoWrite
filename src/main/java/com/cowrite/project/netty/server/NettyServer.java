package com.cowrite.project.netty.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class NettyServer {

    @Value("${netty.server.port:8081}")
    private int port;

    /**
     * 负责接收连接
     */
    private EventLoopGroup bossGroup;

    /**
     * 负责处理连接的数据读写
     */
    private EventLoopGroup workerGroup;

    /**
     * Netty服务器日志
     */
    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    /**
     * 启动Netty服务器
     */
    @PostConstruct
    public void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置TCP参数
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，启动服务器
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("Netty server started on port {}", port);

            // 不阻塞主线程，这里启动后继续执行Spring Boot其他流程
            future.channel().closeFuture().addListener(f -> {
                log.info("Netty server closed");
            });
        } catch (InterruptedException e) {
            log.error("Server interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    @PreDestroy
    public void stop() {
        log.info("Shutting down Netty server...");
        if (bossGroup != null) bossGroup.shutdownGracefully();
        if (workerGroup != null) workerGroup.shutdownGracefully();
    }
}