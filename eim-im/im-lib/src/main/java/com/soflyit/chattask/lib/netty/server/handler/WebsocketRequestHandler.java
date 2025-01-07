package com.soflyit.chattask.lib.netty.server.handler;

import com.alibaba.fastjson.JSON;
import com.soflyit.chattask.lib.netty.cache.WebSocketClientCache;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.config.WebSocketConfig;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.chattask.lib.netty.event.ChatEvent;
import com.soflyit.chattask.lib.netty.event.WebSocketServerHandshakeComplete;
import com.soflyit.chattask.lib.netty.server.service.EventSeqService;
import com.soflyit.common.core.utils.StringUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.soflyit.chattask.lib.netty.common.ChannelAttrKeyConstant.TOKEN_ATTRIBUTE_KEY;
import static com.soflyit.chattask.lib.netty.common.ChatConstant.WEBSOCKET_TOKEN_KEY;

/**
 * 用于解析websocket握手时，请求头中的用户token
 */
@ChannelHandler.Sharable
@Component
@Slf4j
public class WebsocketRequestHandler extends ChannelInboundHandlerAdapter {
    private WebSocketConfig webSocketConfig;

    private EventSeqService eventSeqService;

    private WebSocketClientCache webSocketService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest req = null;
        if (msg instanceof FullHttpRequest) {
            req = (FullHttpRequest) msg;
        } else {
            super.channelRead(ctx, msg);
            return;
        }
        String uri = req.uri();
        if (HttpUtil.is100ContinueExpected(req)) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
            ctx.writeAndFlush(response);
        } else if (!uri.startsWith(webSocketConfig.getPath())) {
            Map<String, Object> data = new HashMap<>();
            data.put("status", "close");
            data.put("error", "无法识别：" + uri);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer(JSON.toJSONString(data).getBytes(Charset.defaultCharset())));
            ctx.writeAndFlush(response); // （6）
            ctx.close();
        } else {
            HttpHeaders headers = req.headers();
            String token = null;
            if (headers != null && headers.contains("Cookie")) {
                String cookies = headers.get("Cookie");
                ServerCookieDecoder decoder = ServerCookieDecoder.STRICT;
                Set<Cookie> cookieSet = decoder.decode(cookies);
                token = processCookie(ctx, cookieSet);
            }
            WebSocketServerHandshakeComplete complete = new WebSocketServerHandshakeComplete(token, ctx.channel().id());
            super.channelRead(ctx, msg);
            ctx.fireUserEventTriggered(complete);
        }
    }


    private String processCookie(ChannelHandlerContext ctx, Set<Cookie> cookieSet) {
        String token = null;
        List<Cookie> cookies = cookieSet.stream().filter(cookie -> cookie != null && StringUtils.equals(cookie.name(), WEBSOCKET_TOKEN_KEY)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(cookies) && cookies.size() == 1 && cookies.get(0) != null) {
            token = cookies.get(0).value();
        }
        ctx.channel().attr(TOKEN_ATTRIBUTE_KEY).set(token);
        return token;
    }

    private void sendAuthReqEvent(ChannelHandlerContext ctx) {
        ChatEvent<HashMap<String, String>, ChatBroadcast<Long, Long, ChannelId>> event = new ChatEvent<>();
        Integer seq = eventSeqService.nextSeq(webSocketService.getClientId(ctx.channel().id()));
        event.setEvent(ChatEventName.AUTH_REQ);
        event.setSeq(seq);
        HashMap<String, String> data = new HashMap<>();
        data.put("server", "1.0");
        event.setData(data);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        event.setBroadcast(broadcast);
        String message = JSON.toJSONString(event);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
        ctx.fireChannelReadComplete();
        log.debug("发送验证事件");
    }

    @Autowired
    public void setWebSocketConfig(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
    }

    @Autowired
    public void setEventSeqService(EventSeqService eventSeqService) {
        this.eventSeqService = eventSeqService;
    }

    @Autowired
    public void setWebSocketService(WebSocketClientCache webSocketService) {
        this.webSocketService = webSocketService;
    }

}