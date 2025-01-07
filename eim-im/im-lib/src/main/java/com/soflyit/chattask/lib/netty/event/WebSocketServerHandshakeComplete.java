package com.soflyit.chattask.lib.netty.event;

import io.netty.channel.ChannelId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WebSocketServerHandshakeComplete {
    private String token;
    private ChannelId channelId;
}
