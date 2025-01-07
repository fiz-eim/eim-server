package com.soflyit.chattask.im.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.channel.mapper.ChatChannelMapper;
import com.soflyit.chattask.im.message.domain.entity.*;
import com.soflyit.chattask.im.message.domain.param.ForwardChannelInfo;
import com.soflyit.chattask.im.message.domain.param.MessageForwardParam;
import com.soflyit.chattask.im.message.domain.vo.ForwardTransactionParam;
import com.soflyit.chattask.im.message.domain.vo.MessageProp;
import com.soflyit.chattask.im.message.mapper.*;
import com.soflyit.common.core.constant.SecurityConstants;
import com.soflyit.common.core.context.SecurityContextHolder;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.soflyit.chattask.im.common.constant.MessageConstant.*;

/**
 * 消息单条转发<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-02-21 16:30
 */
public class ForwardSingleTransactionCallBack implements TransactionCallback<AjaxResult> {

    private MsgForwardServiceImpl msgForwardService;

    private final ForwardTransactionParam forwardTransactionParam;

    private ChatChannelMapper chatChannelMapper;

    private MessageMapper messageMapper;

    private MsgForwardMapper msgForwardMapper;

    private ChannelMemberMapper channelMemberMapper;

    private MsgUnreadMapper msgUnreadMapper;

    private MsgStatisticMapper msgStatisticMapper;

    private MsgFileMapper msgFileMapper;

    public ForwardSingleTransactionCallBack(ForwardTransactionParam forwardTransactionParam) {
        this.forwardTransactionParam = forwardTransactionParam;
    }

    @Override
    public AjaxResult doInTransaction(TransactionStatus status) {
        try {
            LoginUser loginUser = forwardTransactionParam.getLoginUser();
            SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
            SecurityContextHolder.setUserName(loginUser.getUsername());
            SecurityContextHolder.setUserId(String.valueOf(loginUser.getUserid()));

            Map<Integer, List<ForwardChannelInfo>> channalMap = forwardTransactionParam.getChannalMap();
            List<Long> channelIds = new ArrayList<>();
            if (MapUtils.isNotEmpty(channalMap)) {
                List<ForwardChannelInfo> forwardChannelInfos = channalMap.get(FORWARD_CHANNEL_ID_TYPE_CHANNEL);
                if (CollectionUtils.isNotEmpty(forwardChannelInfos)) {
                    forwardChannelInfos.stream().forEach(item -> channelIds.add(item.getDataId()));
                }

                List<ForwardChannelInfo> userIds = channalMap.get(FORWARD_CHANNEL_ID_TYPE_USER);
                if (CollectionUtils.isNotEmpty(userIds)) {
                    msgForwardService.parseUserIds(userIds, SecurityUtils.getLoginUser(), channelIds);
                }

            }
            if (CollectionUtils.isEmpty(channelIds)) {
                return AjaxResult.error("转发消息失败，频道Id不能为空");
            }

            List<Message> messages = forwardTransactionParam.getMessages();
            String realName = forwardTransactionParam.getRealName();

            messages.forEach(message -> {
                forwardSingleMessage(message, channelIds, realName);
            });

            MessageForwardParam forwardParam = forwardTransactionParam.getForwardParam();

            if (StringUtils.isNotEmpty(forwardParam.getContent())) {
                msgForwardService.generateNewMessage(forwardParam, loginUser, channelIds);
            }

            return AjaxResult.success();
        } catch (Exception e) {
            status.setRollbackOnly();
            throw new RuntimeException(e);
        }
    }

    private void forwardSingleMessage(Message message, List<Long> channelIds, String realName) {
        LoginUser loginUser = forwardTransactionParam.getLoginUser();
        Long fowardMsgId = message.getId();

        MsgFile msgFileCondition = new MsgFile();
        msgFileCondition.setMsgId(fowardMsgId);
        List<MsgFile> msgFiles = msgFileMapper.selectMsgFileList(msgFileCondition);

        channelIds.forEach(channelId -> {
            Long forwardMsgId = message.getId();
            message.setId(SnowflakeIdUtil.nextId());
            message.setUserId(loginUser.getUserid());
            message.setChannelId(channelId);
            message.setCreateBy(loginUser.getUserid());
            message.setCreateTime(new Date());
            message.setUpdateBy(loginUser.getUserid());
            message.setUpdateTime(message.getCreateTime());
            message.setCreateUser(realName);
            String propStr = message.getPropsStr();
            MessageProp prop = JSON.parseObject(StringUtils.defaultIfBlank(propStr, "{}"), MessageProp.class);
            prop.setForwardFlag(Boolean.FALSE);
            List<Long> forwardMsgIdList = new ArrayList<>();
            forwardMsgIdList.add(forwardMsgId);
            prop.setForwardMsgIds(forwardMsgIdList);
            message.setPropsStr(JSON.toJSONString(prop, SerializerFeature.WriteClassName));
            messageMapper.insertMessage(message);

            processMsgFile(msgFiles, message);


            MsgForward forward = new MsgForward();
            forward.setId(SnowflakeIdUtil.nextId());
            forward.setMsgId(message.getId());
            forward.setForwardMsgId(fowardMsgId);
            forward.setDeletedFlag(DELETE_FLAG_FALSE);
            forward.setSortOrder(Integer.valueOf(1));
            msgForwardMapper.insertMsgForward(forward);

            Long messageId = message.getId();

            LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>();
            memberQueryWrapper.select(ChannelMember::getUserId);
            memberQueryWrapper.eq(ChannelMember::getChannelId, message.getChannelId());
            memberQueryWrapper.ne(ChannelMember::getUserId, loginUser.getUserid());
            List<ChannelMember> channelMembers = channelMemberMapper.selectList(memberQueryWrapper);
            Integer memberCount = 0;
            List<MsgUnread> unreadList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(channelMembers)) {
                memberCount = channelMembers.size();


                unreadList = new ArrayList<>(channelMembers.size());
                List<MsgUnread> finalUnreadList = unreadList;
                channelMembers.forEach(member -> {
                    MsgUnread unread = new MsgUnread();
                    unread.setId(SnowflakeIdUtil.nextId());
                    unread.setMsgId(messageId);
                    unread.setChannelId(member.getChannelId());
                    unread.setUserId(member.getUserId());
                    finalUnreadList.add(unread);
                });
                msgUnreadMapper.batchInsert(unreadList);
            }
            MsgStatistic stastic = new MsgStatistic();
            stastic.setMsgId(messageId);
            stastic.setUserId(loginUser.getUserid());
            stastic.setUnreadCount(memberCount);

            List<MsgAck> unAckList = new ArrayList<>();

            msgStatisticMapper.insertMsgStatistic(stastic);

            ChatChannel channel = chatChannelMapper.selectChatChannelById(message.getChannelId());
            channel.setUpdateTime(message.getCreateTime());
            channel.setLastMsgTime(message.getCreateTime());
            Long totalMsg = channel.getTotalMsgCount();
            if (totalMsg == null) {
                totalMsg = 0L;
            }
            totalMsg = totalMsg + 1;
            channel.setTotalMsgCount(totalMsg);
            if (message.getRootId() == null) {
                channel.setLastRootMsgTime(message.getCreateTime());
                Long totalRootMsg = channel.getTotalRootMsgCount();
                totalRootMsg = totalRootMsg == null ? 0L : totalRootMsg;
                totalRootMsg = totalRootMsg + 1;
                channel.setTotalRootMsgCount(totalRootMsg);
            }
            chatChannelMapper.updateChatChannel(channel);


            msgForwardService.processPushMessage(message, unreadList, unAckList, null, msgFiles);
        });
    }

    private void processMsgFile(List<MsgFile> msgFiles, Message message) {

        if (CollectionUtils.isNotEmpty(msgFiles)) {
            msgFiles.forEach(item -> {
                item.setId(SnowflakeIdUtil.nextId());
                item.setMsgId(message.getId());
                item.setChannelId(message.getChannelId());
                item.setCreateBy(message.getCreateBy());
                item.setCreateTime(message.getCreateTime());
                item.setUpdateBy(null);
                item.setUpdateTime(null);
            });
            msgFileMapper.batchInsert(msgFiles);
        }
    }

    public void setMsgForwardService(MsgForwardServiceImpl msgForwardService) {
        this.msgForwardService = msgForwardService;
    }

    public void setChatChannelMapper(ChatChannelMapper chatChannelMapper) {
        this.chatChannelMapper = chatChannelMapper;
    }

    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public void setMsgForwardMapper(MsgForwardMapper msgForwardMapper) {
        this.msgForwardMapper = msgForwardMapper;
    }

    public void setChannelMemberMapper(ChannelMemberMapper channelMemberMapper) {
        this.channelMemberMapper = channelMemberMapper;
    }

    public void setMsgUnreadMapper(MsgUnreadMapper msgUnreadMapper) {
        this.msgUnreadMapper = msgUnreadMapper;
    }

    public void setMsgStatisticMapper(MsgStatisticMapper msgStatisticMapper) {
        this.msgStatisticMapper = msgStatisticMapper;
    }

    public void setMsgFileMapper(MsgFileMapper msgFileMapper) {
        this.msgFileMapper = msgFileMapper;
    }
}
