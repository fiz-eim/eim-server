package com.soflyit.chattask.im.event.handler.impl;

import com.alibaba.fastjson.JSON;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.vo.MemberVo;
import com.soflyit.chattask.im.channel.service.IChannelMemberService;
import com.soflyit.chattask.im.config.SoflyImConfig;
import com.soflyit.chattask.im.event.handler.ChatEventHandler;
import com.soflyit.chattask.im.event.handler.EventHandlerManager;
import com.soflyit.chattask.im.netty.service.ChatWebSocketService;
import com.soflyit.chattask.lib.netty.common.ChatEventName;
import com.soflyit.chattask.lib.netty.common.client.WebSocketClientId;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.chattask.lib.netty.event.ChatEvent;
import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import com.soflyit.chattask.lib.netty.server.service.EventSeqService;
import com.soflyit.common.core.utils.StringUtils;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;
import static com.soflyit.chattask.im.event.handler.EventHandlerManager.DEFAULT_EVENT;

/**
 * 默认事件处理器<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-17 10:16
 */
@Slf4j
@Component("defaultEventHandler")
public class DefaultEventHandler<T extends ChatEvent> implements ChatEventHandler<T> {

    protected ChatWebSocketService chatWebSocketService;

    protected EventSeqService eventSeqService;

    protected IChannelMemberService memberService;

    protected final String event = DEFAULT_EVENT;

    protected EventHandlerManager eventHandlerManager;


    protected SoflyImConfig soflyImConfig;



    @Override
    public void processEvent(T event) {
        ChatBroadcast<Long, Long, ChannelId> broadcast = event.getBroadcast();
        ChannelId omitClientId = broadcast.getOmitClientId();
        List<Long> userIds = getUserIdsFromBroadcast(broadcast, event.getEvent());
        List<WebSocketClientId> clientIds = convertUserIdToClientId(userIds, omitClientId);
        log.debug("event - clientIds : {}", JSON.toJSONString(clientIds.stream().map(WebSocketClientId::getClientId).collect(Collectors.toList())));
        if (broadcast.getClientId() != null && !broadcast.getClientId().equals(omitClientId)) {
            WebSocketClientId clientId = chatWebSocketService.getClientId(broadcast.getClientId());
            if (clientId != null) {
                clientIds.add(clientId);
            }
        }
        doFilterClientIds(clientIds, event);
        if (log.isDebugEnabled()) {
            log.debug("处理事件：{}, client:{}", JSON.toJSONString(event), JSON.toJSONString(clientIds));
        }

        if (CollectionUtils.isNotEmpty(clientIds)) {
            clientIds.forEach(webSocketClientId -> {
                int seq = eventSeqService.nextSeq(webSocketClientId);
                event.setSeq(seq);
                String token = event.getToken();
                Long userId = event.getUserId();

                List<WebSocketClientId> currentUserClientIds = null;
                if (userId != null) {
                    currentUserClientIds = chatWebSocketService.getClientIdByUserId(event.getUserId());
                }
                event.setBroadcast(null);
                event.setUserId(null);
                event.setToken(null);

                String message = eventToMessage(event, webSocketClientId, currentUserClientIds);
                try {
                    chatWebSocketService.sendMessageToClient(webSocketClientId, message);
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
                event.setToken(token);
                event.setUserId(userId);
                event.setBroadcast(broadcast);
            });
        }
    }


    protected void doFilterClientIds(List<WebSocketClientId> clientIds, T event) {
        boolean existToken = StringUtils.isNotBlank(event.getToken());
        boolean enableEchoPush = soflyImConfig.getEchoPush() != null && soflyImConfig.getEchoPush();
        boolean isEchoEvent = isEchoPushEvent(event);
        log.debug("过滤客户端, existToken:{}, enableEchoPush:{}, isEchoEvent:{}", existToken, enableEchoPush, isEchoEvent);
        if (!existToken || (enableEchoPush && isEchoEvent)) {
            return;
        }
        WebSocketClientId clientId = chatWebSocketService.getClientIdByToken(event.getToken());
        if (clientId != null) {
            log.debug("过滤客户端:{}", clientId.getClientId());
            clientIds.remove(clientId);
        }
    }


    protected boolean isEchoPushEvent(T event) {
        List<String> eventList = soflyImConfig.getEchoEventList();
        String eventName = event.getEvent();
        return StringUtils.isNotEmpty(eventName) && CollectionUtils.isNotEmpty(eventList) && eventList.contains(eventName);
    }


    protected List<WebSocketClientId> convertUserIdToClientId(List<Long> userIds, ChannelId omitClientId) {
        List<WebSocketClientId> clientIds = new ArrayList<>(200);
        if (CollectionUtils.isNotEmpty(userIds)) {
            userIds.forEach(userId -> {
                List<WebSocketClientId> userClientIds = chatWebSocketService.getClientIdByUserId(userId);
                if (CollectionUtils.isNotEmpty(userClientIds)) {
                    userClientIds.forEach(item -> {
                        if (item != null && !item.equals(omitClientId)) {
                            log.debug("userId:{} to clientId:{}", userId, item.getClientId());
                            clientIds.add(item);
                        }
                    });
                }
            });
        }
        return clientIds;
    }

    protected String eventToMessage(T event, WebSocketClientId webSocketClientId, List<WebSocketClientId> currentUserClientIds) {
        return JSON.toJSONString(event, WriteMapNullValue);
    }


    protected List<Long> getUserIdsFromBroadcast(ChatBroadcast<Long, Long, ChannelId> broadcast, String event) {

        Long channelId = broadcast.getChannelId();
        Map<Long, Boolean> omitUsers = broadcast.getOmitUsers();
        Set<Long> userIds = new HashSet<>(200);

        if (CollectionUtils.isNotEmpty(broadcast.getUserIds())) {
            userIds.addAll(broadcast.getUserIds());
        }

        if (channelId != null) {
            ChannelMember condition = new ChannelMember();
            condition.setChannelId(channelId);
            if (StringUtils.equals(ChatEventName.CHANNEL_DELETE_EVENT, event)) {
                condition.getParams().put("includeDeleted", Boolean.TRUE);
            }
            List<MemberVo> members = memberService.selectChannelMemberList(condition);
            if (CollectionUtils.isNotEmpty(members)) {
                members.forEach(member -> {
                    if (omitUsers == null || omitUsers.get(member.getUserId()) == null || !omitUsers.get(member.getUserId())) {
                        userIds.add(member.getUserId());
                    }
                });
            }
        }
        if (broadcast.getUserId() != null) {
            if (omitUsers == null || omitUsers.get(broadcast.getUserId()) == null || !omitUsers.get(broadcast.getUserId())) {
                userIds.add(broadcast.getUserId());
            }
        }

        return new ArrayList<>(userIds);
    }

    protected Long getUserIdFromClientId(WebSocketClientId clientId) {
        WebsocketUserId<Long> websocketUserId = chatWebSocketService.getUserIdByChannelId(clientId);
        if(websocketUserId != null) {
            return websocketUserId.getUserId();
        }
        return null;
    }

    @PostConstruct
    private void registerToManager() {
        String event = getEvent();
        eventHandlerManager.registerEventHandlerClass(event, this.getClass());
    }

    @Autowired
    public void setChatWebSocketService(ChatWebSocketService chatWebSocketService) {
        this.chatWebSocketService = chatWebSocketService;
    }

    @Autowired
    public void setEventSeqService(EventSeqService eventSeqService) {
        this.eventSeqService = eventSeqService;
    }

    @Autowired
    public void setMemberService(IChannelMemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setEventHandlerManager(EventHandlerManager eventHandlerManager) {
        this.eventHandlerManager = eventHandlerManager;
    }

    @Autowired
    public void setSoflyImConfig(SoflyImConfig soflyImConfig) {
        this.soflyImConfig = soflyImConfig;
    }

    @Override
    public String getEvent() {
        return event;
    }


}
