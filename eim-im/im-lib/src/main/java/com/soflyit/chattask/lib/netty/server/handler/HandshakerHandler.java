package com.soflyit.chattask.lib.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.common.client.NettyWebSocketClient;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClient;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClientId;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.chattask.lib.netty.event.ChatEvent;
import com.soflyit.chattask.lib.netty.event.WebSocketServerHandshakeComplete;
import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import com.soflyit.chattask.lib.netty.server.service.EventSeqService;
import com.soflyit.chattask.lib.netty.server.service.WebSocketService;
import com.soflyit.common.core.utils.StringUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.soflyit.chattask.lib.netty.common.ChannelAttrKeyConstant.TOKEN_ATTRIBUTE_KEY;
import static com.soflyit.chattask.lib.netty.common.ChannelAttrKeyConstant.USER_ID_ATTRIBUTE_KEY;

/**
 * websocket 握手成功事件处理器<br>
 * websocket 握手成功后，执行鉴权操作；先根据cookie中的token进行鉴权，如果token不存在则向客户端发送请求鉴权事件，请求客户端发送token信息进行鉴权
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class HandshakerHandler extends SimpleChannelInboundHandler<WebSocketServerHandshakeComplete> {


    private WebSocketService webSocketService;

    private EventSeqService eventSeqService;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerHandshakeComplete) {
            WebSocketServerHandshakeComplete msg = (WebSocketServerHandshakeComplete) evt;
            String token = msg.getToken();
            Channel channel = ctx.channel();

            Integer seq = eventSeqService.nextSeq(webSocketService.getClientId(ctx.channel().id()));

            if (StringUtils.isNotBlank(token)) {
                WebsocketUserId<Long> userId = webSocketService.validateToken(token);
                ChatEvent<HashMap<String, String>, ChatBroadcast<Long, Long, ChannelId>> event = new ChatEvent<>();


                if (userId == null) {
                    event.setEvent(ChatEventName.AUTH_REQ);
                    event.setSeq(seq);
                    HashMap<String, String> data = new HashMap<>();

                    data.put("server", "1.0");
                    data.put("msg", "websocket.login.failed.token-is-expire");
                    event.setData(data);

                    ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
                    broadcast.setClientId(ctx.channel().id());
                    event.setBroadcast(broadcast);
                } else {

                    ctx.channel().attr(USER_ID_ATTRIBUTE_KEY).set(userId);


                    event.setEvent(ChatEventName.HELLO);
                    event.setSeq(seq);
                    HashMap<String, String> data = new HashMap<>();

                    data.put("server", "1.0");
                    event.setData(data);

                    ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
                    broadcast.setUserId(userId.getUserId());
                    event.setBroadcast(broadcast);

                    channel.attr(TOKEN_ATTRIBUTE_KEY).set(token);

                    WebSocketClientId<ChannelId> clientId = new WebSocketClientId(channel.id());
                    WebSocketClient<Channel, ChannelId, String> client = new NettyWebSocketClient(channel, clientId);
                    webSocketService.doCacheClient(client, userId, token);
                    webSocketService.doConnect(clientId, userId);

                }
                ctx.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(event)));

            } else {
                ChatEvent<HashMap<String, String>, ChatBroadcast<Long, Long, ChannelId>> event = new ChatEvent<>();
                event.setEvent(ChatEventName.AUTH_REQ);
                event.setSeq(seq);
                HashMap<String, String> data = new HashMap<>();

                data.put("server", "1.0");
                event.setData(data);

                ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
                broadcast.setClientId(ctx.channel().id());
                event.setBroadcast(broadcast);
                ctx.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(event)));
            }
        }
        super.userEventTriggered(ctx, evt);
    }


    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Autowired
    public void setEventSeqService(EventSeqService eventSeqService) {
        this.eventSeqService = eventSeqService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketServerHandshakeComplete msg) throws Exception {

    }
}
