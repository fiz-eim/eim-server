package com.soflyit.chattask.im.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.event.domain.MessagePinnedEvent;
import com.soflyit.chattask.im.event.domain.MessageUnPinnedEvent;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgPinned;
import com.soflyit.chattask.im.message.domain.vo.MessageProp;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.im.message.mapper.MessageMapper;
import com.soflyit.chattask.im.message.mapper.MsgPinnedMapper;
import com.soflyit.chattask.im.message.service.IMsgPinnedService;
import com.soflyit.chattask.im.system.service.impl.UserNickNameService;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 固定消息Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Service
@Slf4j
public class MsgPinnedServiceImpl implements IMsgPinnedService {

    private ChatEventService chatEventService;

    private MsgPinnedMapper msgPinnedMapper;

    private MessageMapper messageMapper;

    private ChannelMemberMapper channelMemberMapper;

    @Autowired
    private UserNickNameService nickNameService;

    @Autowired
    private MessageQueryService messageQueryService;


    @Override
    public MsgPinned selectMsgPinnedById(Long id) {
        return msgPinnedMapper.selectMsgPinnedById(id);
    }


    @Override
    public List<MsgPinned> selectMsgPinnedList(MsgPinned msgPinned) {
        return msgPinnedMapper.selectMsgPinnedList(msgPinned);
    }


    @Override
    public int insertMsgPinned(MsgPinned msgPinned) {
        msgPinned.setCreateTime(DateUtils.getNowDate());
        return msgPinnedMapper.insertMsgPinned(msgPinned);
    }


    @Override
    public int updateMsgPinned(MsgPinned msgPinned) {
        msgPinned.setUpdateTime(DateUtils.getNowDate());
        return msgPinnedMapper.updateMsgPinned(msgPinned);
    }


    @Override
    public int deleteMsgPinnedByIds(Long[] ids) {
        return msgPinnedMapper.deleteMsgPinnedByIds(ids);
    }


    @Override
    public int deleteMsgPinnedById(Long id) {
        return msgPinnedMapper.deleteMsgPinnedById(id);
    }


    @Override
    public AjaxResult getPinnedMessages(Long channelId) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("查询固定消息失败：无法获取当前用户信息");
        }


        MsgPinned msgPinnedCondition = new MsgPinned();
        msgPinnedCondition.setChannelId(channelId);
        List<MsgPinned> existDatas = msgPinnedMapper.selectMsgPinnedList(msgPinnedCondition);
        if (CollectionUtils.isNotEmpty(existDatas)) {
            List<Long> messageIds = existDatas.stream().map(MsgPinned::getMsgId).collect(Collectors.toList());

            LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Message::getId, messageIds);

            List<Message> messageList = messageMapper.selectList(queryWrapper);

            List<Long> ackMessageIds = new ArrayList<>(messageList.size());
            List<Long> tagMsgIds = new ArrayList<>();

            List<MessageVo> msgList = messageQueryService.processMetaData(messageList, messageIds, ackMessageIds, tagMsgIds, null);

            return AjaxResult.success(msgList);

        }

        return AjaxResult.success(new ArrayList<>());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult pinnedMessage(Long msgId) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        Message message = messageMapper.selectMessageById(msgId);

        if (message == null) {
            String msg = "固定消息失败，消息不存在";
            return AjaxResult.error(msg);
        }

        if (message.getDeleteTime() > 0) {
            String msg = "固定消息失败，消息已删除";
            return AjaxResult.error(msg);
        }

        ChannelMember member = new ChannelMember();
        member.setUserId(loginUser.getUserid());
        member.setChannelId(message.getChannelId());
        List<ChannelMember> channelMembers = channelMemberMapper.selectChannelMemberList(member);
        if (CollectionUtils.isEmpty(channelMembers)) {
            String msg = "固定消息失败，没有权限";
            return AjaxResult.error(msg);
        }


        MsgPinned msgPinnedCondition = new MsgPinned();
        msgPinnedCondition.setMsgId(msgId);
        msgPinnedCondition.setUserId(loginUser.getUserid());
        List<MsgPinned> existDatas = msgPinnedMapper.selectMsgPinnedList(msgPinnedCondition);
        if (CollectionUtils.isNotEmpty(existDatas)) {
            return AjaxResult.success();
        }

        MessageProp prop = JSON.parseObject(message.getPropsStr(), MessageProp.class);
        prop.setPinnedFlag(Boolean.TRUE);
        prop.setPinnedUserId(loginUser.getUserid());
        prop.setPinnedUser(nickNameService.getNickName(loginUser.getUserid()));
        message.setPropsStr(JSON.toJSONString(prop, SerializerFeature.WriteClassName));
        messageMapper.updateMessage(message);

        MsgPinned msgPinned = new MsgPinned();
        msgPinned.setId(SnowflakeIdUtil.nextId());
        msgPinned.setUserId(loginUser.getUserid());
        msgPinned.setChannelId(member.getChannelId());
        msgPinned.setMsgId(msgId);
        msgPinnedMapper.insertMsgPinned(msgPinned);
        processPinMessage(message);
        return AjaxResult.success();
    }


    @Override
    public AjaxResult unpinnedMessage(Long msgId) {

        LoginUser loginUser = SecurityUtils.getLoginUser();

        Message message = messageMapper.selectMessageById(msgId);
        if (message != null) {
            ChannelMember member = new ChannelMember();
            member.setUserId(loginUser.getUserid());
            member.setChannelId(message.getChannelId());
            List<ChannelMember> channelMembers = channelMemberMapper.selectChannelMemberList(member);
            if (CollectionUtils.isEmpty(channelMembers)) {
                String msg = "取消固定消息失败，没有权限";
                return AjaxResult.error(msg);
            }
        }

        MsgPinned msgPinnedCondition = new MsgPinned();
        msgPinnedCondition.setMsgId(msgId);
        msgPinnedCondition.setUserId(loginUser.getUserid());
        List<MsgPinned> existDatas = msgPinnedMapper.selectMsgPinnedList(msgPinnedCondition);
        if (CollectionUtils.isEmpty(existDatas)) {
            return AjaxResult.success();
        }

        Set<Long> pinnedIds = existDatas.stream().map(MsgPinned::getId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(pinnedIds)) {
            msgPinnedMapper.deleteMsgPinnedByIds(pinnedIds.toArray(new Long[pinnedIds.size()]));
        }

        MessageProp prop = JSON.parseObject(message.getPropsStr(), MessageProp.class);
        prop.setPinnedFlag(Boolean.FALSE);
        prop.setPinnedUserId(null);
        prop.setPinnedUser(null);
        message.setPropsStr(JSON.toJSONString(prop, SerializerFeature.WriteClassName));
        messageMapper.updateMessage(message);

        processUnPinMessage(message);
        return AjaxResult.success();
    }


    private void processUnPinMessage(Message message) {
        if (message == null) {
            log.warn("生成固定消息事件失败，消息为null");
            return;
        }

        MessageUnPinnedEvent event = new MessageUnPinnedEvent();
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        List<Long> messageIds = new ArrayList<>();
        messageIds.add(message.getId());
        List<MessageVo> messageVos = messageQueryService.processMetaData(messages, messageIds, null, null, null);
        event.setData(messageVos.get(0));

        ChatBroadcast<Long, Long, ChannelId> messageBroadcast = new ChatBroadcast();
        messageBroadcast.setChannelId(message.getChannelId());

        event.setBroadcast(messageBroadcast);

        chatEventService.doProcessEvent(event);
    }


    private void processPinMessage(Message message) {
        if (message == null) {
            log.warn("生成固定消息事件失败，消息为null");
            return;
        }
        MessagePinnedEvent event = new MessagePinnedEvent();
        List<Message> messages = new ArrayList<>();
        messages.add(message);
        List<Long> messageIds = new ArrayList<>();
        messageIds.add(message.getId());
        List<MessageVo> messageVos = messageQueryService.processMetaData(messages, messageIds, null, null, null);
        event.setData(messageVos.get(0));

        ChatBroadcast<Long, Long, ChannelId> messageBroadcast = new ChatBroadcast();
        messageBroadcast.setChannelId(message.getChannelId());
        event.setBroadcast(messageBroadcast);
        chatEventService.doProcessEvent(event);
    }

    @Autowired
    public void setChatEventService(ChatEventService chatEventService) {
        this.chatEventService = chatEventService;
    }

    @Autowired
    public void setMsgPinnedMapper(MsgPinnedMapper msgPinnedMapper) {
        this.msgPinnedMapper = msgPinnedMapper;
    }

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Autowired
    public void setChannelMemberMapper(ChannelMemberMapper channelMemberMapper) {
        this.channelMemberMapper = channelMemberMapper;
    }
}
