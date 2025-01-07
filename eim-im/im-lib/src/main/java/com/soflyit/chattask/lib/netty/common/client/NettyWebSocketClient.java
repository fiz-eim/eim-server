package com.soflyit.chattask.lib.netty.common.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;


/**
 * netty 客户端：封装nett通道、通道Id
 */
@Slf4j
public class NettyWebSocketClient extends WebSocketClient<Channel, ChannelId, String> {

    public NettyWebSocketClient(Channel client, WebSocketClientId<ChannelId> clientId) {
        super(client, clientId);
    }

    @Override
    public void sendMessage(String message) {
        Channel channel = getClient();
        log.debug("client send message: {}, {}", channel.id(), message);
        TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(message);
        channel.writeAndFlush(webSocketFrame);
    }
}
