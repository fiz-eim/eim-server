package com.soflyit.chattask.lib.netty.server;

import com.soflyit.chattask.lib.netty.config.WebSocketConfig;
import com.soflyit.chattask.lib.netty.server.handler.ChatDataHandler;
import com.soflyit.chattask.lib.netty.server.handler.HandshakerHandler;
import com.soflyit.chattask.lib.netty.server.handler.WebsocketRequestHandler;
import com.soflyit.chattask.lib.netty.server.service.ChatWebSocketServerCallBack;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * websocket服务
 */
@Component
@Slf4j
public class ChatWebSocketServer implements ApplicationContextAware, Runnable, ApplicationListener<ContextClosedEvent> {


    private WebSocketConfig webSocketConfig;

    private ApplicationContext applicationContext;


    private Channel serverChannel;

    @Value("${spring.application.name}")
    private String serverName;



    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();

                    pipeline.addLast(new HttpServerCodec());
                    pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                    pipeline.addLast(new ChunkedWriteHandler());
                    pipeline.addLast(applicationContext.getBean(WebsocketRequestHandler.class));
                    pipeline.addLast(new WebSocketServerProtocolHandler(webSocketConfig.getPath()));
                    pipeline.addLast(applicationContext.getBean(ChatDataHandler.class));
                    pipeline.addLast(applicationContext.getBean(HandshakerHandler.class));

                }
            });
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);    // (5)
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
            Channel channel = serverBootstrap.bind(webSocketConfig.getPort()).sync().channel();
            this.serverChannel = channel;
            log.info("websocket 服务启动，ip={},port={}", webSocketConfig.getIp(), webSocketConfig.getPort());
            ChatWebSocketServerCallBack serverCallBack = null;
            try {
                serverCallBack = applicationContext.getBean(ChatWebSocketServerCallBack.class);
            } catch (BeansException e) {
                log.warn(e.getMessage(), e);
            }
            serverStarted(serverCallBack, webSocketConfig.getPort());
            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    private void serverStarted(ChatWebSocketServerCallBack serverCallBack, int port) {
        if (serverCallBack != null) {
            serverCallBack.start(port);
        }

    }


    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (this.serverChannel != null) {
            this.serverChannel.close();
        }
        log.info("websocket 服务停止");
    }

    @Autowired
    public void setWebSocketConfig(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public void run() {
        try {
            start();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}