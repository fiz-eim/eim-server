package com.soflyit.chattask.lib.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.soflyit.chattask.lib.netty.action.ChatActionRequest;
import com.soflyit.chattask.lib.netty.action.ChatActionResponse;
import com.soflyit.chattask.lib.netty.action.domain.AuthActionRequest;
import com.soflyit.chattask.lib.netty.action.domain.AuthActionResponse;
import com.soflyit.chattask.lib.netty.action.domain.AuthActionResult;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.common.client.NettyWebSocketClient;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClient;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClientId;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.chattask.lib.netty.event.ChatEvent;
import com.soflyit.chattask.lib.netty.server.domain.MessageProcessResult;
import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import com.soflyit.chattask.lib.netty.server.service.EventSeqService;
import com.soflyit.chattask.lib.netty.server.service.WebSocketService;
import com.soflyit.common.core.utils.StringUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.soflyit.chattask.lib.netty.common.ActionStatusConstant.FAIL;
import static com.soflyit.chattask.lib.netty.common.ActionStatusConstant.OK;
import static com.soflyit.chattask.lib.netty.common.ChannelAttrKeyConstant.TOKEN_ATTRIBUTE_KEY;
import static com.soflyit.chattask.lib.netty.common.ChannelAttrKeyConstant.USER_ID_ATTRIBUTE_KEY;
import static com.soflyit.chattask.lib.netty.common.ChatEventName.AUTH_REQ;

/**
 * websocket 请求数据处理
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class ChatDataHandler extends SimpleChannelInboundHandler<WebSocketFrame> {


    private WebSocketService webSocketService;


    private EventSeqService eventSeqService;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) {

        if (msg instanceof PingWebSocketFrame) {
            PongWebSocketFrame pongWebSocketFrame = new PongWebSocketFrame();
            ctx.channel().writeAndFlush(pongWebSocketFrame);
        } else if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;

            WebSocketClientId<ChannelId> clientId = new WebSocketClientId(ctx.channel().id());
            Attribute<String> attribute = ctx.channel().attr(TOKEN_ATTRIBUTE_KEY);
            String token = null;
            if (attribute != null) {
                token = attribute.get();
            }
            MessageProcessResult result = this.webSocketService.processMessage(clientId, textWebSocketFrame.text(), token);

            doCheckResult(ctx, result);
        } else {

            ChatActionResponse<String> actionResponse = new ChatActionResponse<>();
            actionResponse.setStatus(FAIL);
            actionResponse.setMessage("消息处理失败，暂不支持【" + msg.getClass().getSimpleName() + "】类型消息");
            TextWebSocketFrame response = new TextWebSocketFrame(JSON.toJSONString(actionResponse));

            ctx.channel().writeAndFlush(response);
        }
    }


    private void doCheckResult(ChannelHandlerContext ctx, MessageProcessResult result) {
        if (result == null) {
            return;
        }
        ChatActionRequest request = result.getRequestData();
        ChatActionResponse response = result.getResponseData();
        if (StringUtils.equals(request.getAction(), AUTH_REQ) && StringUtils.equals(response.getStatus(), OK)) {
            sendHello(ctx, ((MessageProcessResult<AuthActionRequest, AuthActionResponse, AuthActionResult>) result).getData());
        } else {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(response)));
        }


    }

    private void sendHello(ChannelHandlerContext ctx, AuthActionResult authActionResult) {

        Channel channel = ctx.channel();
        WebsocketUserId<Long> userId = authActionResult.getWebsocketUserId();
        channel.attr(USER_ID_ATTRIBUTE_KEY).set(userId);
        channel.attr(TOKEN_ATTRIBUTE_KEY).set(authActionResult.getToken());
        ChatEvent<HashMap<String, String>, ChatBroadcast<Long, Long, ChannelId>> event = new ChatEvent<>();

        WebSocketClientId<ChannelId> clientId = webSocketService.getClientId(channel.id());
        Integer seq = eventSeqService.nextSeq(clientId);


        event.setEvent(ChatEventName.HELLO);
        event.setSeq(seq);
        HashMap<String, String> data = new HashMap<>();
        data.put("server", "1.0");
        event.setData(data);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        broadcast.setUserId(userId.getUserId());
        event.setBroadcast(broadcast);
        channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(event)));


        WebSocketClient<Channel, ChannelId, String> client = new NettyWebSocketClient(channel, clientId);
        webSocketService.doCacheClient(client, userId, authActionResult.getToken());
        webSocketService.doConnect(clientId, userId);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        WebSocketClientId<ChannelId> clientId = new WebSocketClientId(channel.id());
        WebSocketClient<Channel, ChannelId, String> client = new WebSocketClient<>(channel, clientId);
        webSocketService.removeChannel(client);
        webSocketService.doDisconnect(clientId);
        log.info("链接断开：{}", channel.remoteAddress());
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Channel channel = ctx.channel();
        log.info("链接创建：{}", channel.remoteAddress());
    }

    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Autowired
    public void setEventSeqService(EventSeqService eventSeqService) {
        this.eventSeqService = eventSeqService;
    }
}