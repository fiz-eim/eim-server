package com.soflyit.chattask.im.message.service.impl;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.event.domain.*;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.im.message.domain.vo.MessageProp;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.im.message.domain.vo.MsgTopVo;
import com.soflyit.chattask.im.message.domain.vo.SystemMessageData;
import com.soflyit.chattask.im.message.domain.vo.sysdata.AddMemberData;
import com.soflyit.chattask.im.message.domain.vo.sysdata.RemoveMemberData;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MessagePushService {

    @Autowired
    private ChatEventService chatEventService;

    public void pushCreateTopicEvent(Long msgId, Long channelId) {

        TopicCreateEvent topicCreateEvent = new TopicCreateEvent();
        topicCreateEvent.setData(msgId);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast();
        broadcast.setChannelId(channelId);

        topicCreateEvent.setBroadcast(broadcast);
        chatEventService.doProcessEvent(topicCreateEvent);

    }


    private void processBroadcastByProp(MessageProp prop, ChatBroadcast<Long, Long, ChannelId> messageBroadcast) {
        SystemMessageData systemMessageData = prop.getSystemData();
        if (systemMessageData instanceof AddMemberData) {
            List<ChannelMember> memberList = ((AddMemberData<?>) systemMessageData).getMemberList();
            if (CollectionUtils.isEmpty(memberList)) {
                return;
            }
            Map<Long, Boolean> omitUserMap = new HashMap<>();
            memberList.forEach(member -> {
                omitUserMap.put(member.getUserId(), Boolean.TRUE);
            });
            messageBroadcast.setOmitUsers(omitUserMap);
        } else if (systemMessageData instanceof RemoveMemberData) {
            List<ChannelMember> memberList = ((RemoveMemberData<?>) systemMessageData).getMemberList();
            if (CollectionUtils.isEmpty(memberList)) {
                return;
            }
            Map<Long, Boolean> omitUserMap = new HashMap<>();
            memberList.forEach(member -> {
                omitUserMap.put(member.getUserId(), Boolean.TRUE);
            });
            messageBroadcast.setOmitUsers(omitUserMap);
        }
    }

    public void pushMessageTopEvent(MsgTopVo message) {
        MessageTopEvent msgTopEvent = new MessageTopEvent();
        msgTopEvent.setData(message);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast();
        broadcast.setChannelId(message.getChannelId());

        msgTopEvent.setBroadcast(broadcast);
        chatEventService.doProcessEvent(msgTopEvent);

    }


    public void pushMessageCancelTopEvent(MsgTopVo message) {
        MessageCancelTopEvent msgTopEvent = new MessageCancelTopEvent();
        msgTopEvent.setData(message);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast();
        broadcast.setChannelId(message.getChannelId());

        msgTopEvent.setBroadcast(broadcast);
        chatEventService.doProcessEvent(msgTopEvent);
    }

    public void pushMemberDeleteMsgEvent(MessageVo messageVo, Long userId) {


        MemberDeleteMsgEvent memberDeleteMsgEvent = new MemberDeleteMsgEvent();
        memberDeleteMsgEvent.setData(messageVo);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast();
        broadcast.setUserId(userId);
        memberDeleteMsgEvent.setBroadcast(broadcast);
        chatEventService.doProcessEvent(memberDeleteMsgEvent);
    }


    public void pushMemberUpdateEvent(ChannelMember member, Boolean channelType) {

        ChannelMemberUpdateEvent memberUpdateEvent = new ChannelMemberUpdateEvent();
        memberUpdateEvent.setData(member);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        if (channelType != null && channelType) {
            broadcast.setChannelId(member.getChannelId());
        } else {
            broadcast.setUserId(member.getUserId());
        }
        memberUpdateEvent.setBroadcast(broadcast);
        chatEventService.doProcessEvent(memberUpdateEvent);
    }




    public void pushMessageTextTagEvent(MessageVo message) {

        MessageTextTagEvent textTagEvent = new MessageTextTagEvent();

        textTagEvent.setData(message);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        broadcast.setChannelId(message.getChannelId());

        textTagEvent.setBroadcast(broadcast);
        chatEventService.doProcessEvent(textTagEvent);
    }
    @Autowired
    public void setChatEventService(ChatEventService chatEventService) {
        this.chatEventService = chatEventService;
    }

}
