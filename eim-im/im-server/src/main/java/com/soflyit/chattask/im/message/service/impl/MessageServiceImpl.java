package com.soflyit.chattask.im.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.soflyit.chattask.im.bot.service.IImCardService;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.domain.vo.BanConfig;
import com.soflyit.chattask.im.channel.domain.vo.ChannelExtData;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.channel.mapper.ChatChannelMapper;
import com.soflyit.chattask.im.channel.service.IChannelMemberService;
import com.soflyit.chattask.im.common.util.MessageMetaUtil;
import com.soflyit.chattask.im.event.domain.MessagePostedEvent;
import com.soflyit.chattask.im.event.domain.MessageReadEvent;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.im.message.domain.entity.*;
import com.soflyit.chattask.im.message.domain.param.*;
import com.soflyit.chattask.im.message.domain.vo.*;
import com.soflyit.chattask.im.message.domain.vo.sysdata.AddMemberData;
import com.soflyit.chattask.im.message.domain.vo.sysdata.RecallMessageData;
import com.soflyit.chattask.im.message.domain.vo.sysdata.RemoveMemberData;
import com.soflyit.chattask.im.message.mapper.*;
import com.soflyit.chattask.im.message.service.*;
import com.soflyit.chattask.im.system.service.impl.UserNickNameService;
import com.soflyit.chattask.lib.netty.action.domain.ReadMessageActionRequest;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.common.core.constant.SecurityConstants;
import com.soflyit.common.core.context.SecurityContextHolder;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.security.auth.AuthUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.soflyit.chattask.im.common.constant.ChannelConstant.*;
import static com.soflyit.chattask.im.common.constant.MessageConstant.*;

/**
 * 消息Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Service
@Slf4j
public class MessageServiceImpl implements IMessageService {

    private MessageMapper messageMapper;

    private MsgUnreadMapper msgUnreadMapper;

    private MsgStatisticMapper statisticMapper;

    private MsgAckMapper msgAckMapper;

    private ChannelMemberMapper channelMemberMapper;

    private ChatEventService chatEventService;

    private MsgFileMapper msgFileMapper;

    private ChatChannelMapper chatChannelMapper;

    private UserNickNameService userNickNameService;

    private IMsgPinnedService msgPinnedService;

    private IMsgReplyService msgReplyService;

    private MessageQueryService messageQueryService;

    private IMsgUnreadService msgUnreadService;

    private MessagePushService messagePushService;

    private IMsgReactionService reactionService;

    private IMsgTopService msgTopService;


    @Override
    public Message selectMessageById(Long id) {
        return messageMapper.selectMessageById(id);
    }


    @Override
    public List<Message> selectMessageList(Message message) {
        return messageMapper.selectMessageList(message);
    }


    @Override
    public int insertMessage(Message message) {
        return messageMapper.insertMessage(message);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateMessage(Message message) {
        throw new BaseException("暂不支持修改消息");
    }


    @Override
    public int deleteMessageByIds(Long[] ids) {
        return messageMapper.deleteMessageByIds(ids);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult deleteMessageById(Long id) {

        Message deletedMessage = messageMapper.selectMessageById(id);

        Message deletedMessageCopy = new Message();
        BeanUtils.copyBeanProp(deletedMessageCopy, deletedMessage);
        if (deletedMessage == null) {
            String msg = "撤回消息失败，消息不存在";
            return AjaxResult.error(msg);
        } else if (deletedMessage.getDeleteTime() > 0) {
            String msg = "撤回消息失败，消息已删除";
            return AjaxResult.success(msg);
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();
        ChannelMember member = new ChannelMember();
        member.setUserId(loginUser.getUserid());
        member.setChannelId(deletedMessage.getChannelId());

        deletedMessage.setDeleteBy(SecurityUtils.getUserId());
        deletedMessage.setDeleteTime(System.currentTimeMillis());
        deletedMessage.setId(SnowflakeIdUtil.nextId());

        messageMapper.insertMessage(deletedMessage);

        messageMapper.deleteMessageById(id);

        processMessageDelete(deletedMessageCopy, SecurityUtils.getUserId(), deletedMessage.getId());
        return AjaxResult.success();
    }


    @Override
    public AjaxResult pinnedMessage(Long msgId) {
        return msgPinnedService.pinnedMessage(msgId);
    }



    @Override
    public AjaxResult unpinnedMessage(Long msgId) {
        return msgPinnedService.unpinnedMessage(msgId);
    }

    @Override
    public AjaxResult getPinnedMessages(Message message) {
        if (message == null) {
            return AjaxResult.error("查询固定消息列表失败：参数不能为空");
        }
        Long channelId = message.getChannelId();
        if (channelId == null) {
            return AjaxResult.error("查询固定消息列表失败：群组Id不能为空");
        }
        return msgPinnedService.getPinnedMessages(channelId);
    }


    @Override
    public AjaxResult<List<MessageVo>> selectThreadMessages(Long msgId, ThreadQueryParam threadQueryParam) {
        return messageQueryService.selectThreadMessages(msgId, threadQueryParam);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult readMessage(Message message, ReadMessageActionRequest request) {


        Message messageInDb = messageMapper.selectMessageById(message.getId());
        if (messageInDb == null) {
            String errMsg = "读取消息失败，消息不存在";
            return AjaxResult.error(errMsg);
        }

        ChatChannel channel = chatChannelMapper.selectChatChannelById(messageInDb.getChannelId());
        if (channel == null) {
            String errMsg = "读取消息失败，频道不存在";
            return AjaxResult.error(errMsg);
        }
        if (channel.getDeleteTime() > -1L) {
            String errMsg = "读取消息失败，频道已删除";
            return AjaxResult.error(errMsg);
        }






        Long userId = request.getWebsocketUserId().getUserId();

        LambdaQueryWrapper<MsgUnread> unreadQueryWrapper = new LambdaQueryWrapper<>(MsgUnread.class);
        unreadQueryWrapper.eq(MsgUnread::getMsgId, messageInDb.getId());
        unreadQueryWrapper.eq(MsgUnread::getUserId, userId);
        MsgUnread unreadData = msgUnreadMapper.selectOne(unreadQueryWrapper);
        if (unreadData.getReadTime() == null) {
            unreadData.setReadTime(new Date());
            unreadData.setUpdateBy(userId);
            msgUnreadMapper.updateMsgUnread(unreadData);
        }
        unreadQueryWrapper.clear();
        unreadQueryWrapper.select(MsgUnread::getUserId);
        unreadQueryWrapper.eq(MsgUnread::getMsgId, messageInDb.getId());
        unreadQueryWrapper.isNull(MsgUnread::getReadTime);
        Long unreadCount = msgUnreadMapper.selectCount(unreadQueryWrapper);

        MsgStatistic statistic = statisticMapper.selectMsgStatisticByMsgId(messageInDb.getId());
        statistic.setUnreadCount(unreadCount.intValue());
        statisticMapper.updateMsgStatistic(statistic);

        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>(ChannelMember.class);
        memberQueryWrapper.eq(ChannelMember::getChannelId, messageInDb.getChannelId());
        memberQueryWrapper.eq(ChannelMember::getUserId, userId);
        List<ChannelMember> members = channelMemberMapper.selectList(memberQueryWrapper);
        ChannelMember member = members.remove(0);

        Long unreadMsgCount = member.getUnreadCount();
        if (unreadMsgCount == null) {
            unreadMsgCount = 1L;
        }
        if (unreadMsgCount > 0L) {
            unreadMsgCount--;
        }
        member.setUnreadCount(unreadMsgCount);
        boolean isRoot = messageInDb.getRootId() == null;
        if (isRoot) {
            Long unreadRootCount = member.getRootMsgCount();
            if (unreadRootCount == null) {
                unreadRootCount = 1L;
            }
            if (unreadRootCount > 0L) {
                unreadRootCount--;
            }
            member.setRootMsgCount(unreadRootCount);
        }

        String mentionUserStr = messageInDb.getMentionUsers();
        if (mentionUserStr != null) {
            List<Long> mentionUsers = JSON.parseArray(mentionUserStr, Long.class);
            processMentionForRead(userId, mentionUsers, member, isRoot);
        }
        member.setLastViewTime(new Date());
        channelMemberMapper.updateChannelMember(member);


        processMessageRead(messageInDb, userId, request.getToken());

        return AjaxResult.success();
    }


    @Override
    public AjaxResult ackMessage(Long msgId) {
        return AjaxResult.error("暂不支持该操作");
    }

    @Override
    public AjaxResult cancelAckMessage(Long msgId) {
        return AjaxResult.error("暂不支持该操作");
    }


    @Override
    public AjaxResult createTopic(Long msgId) {
        if (msgId == null) {
            return AjaxResult.error("创建话题失败，消息Id不能为空");
        }

        Message message = messageMapper.selectMessageById(msgId);
        if (message == null) {
            return AjaxResult.error("创建话题失败，消息不存在");
        }

        if (message.getDeleteTime() > -1L) {
            return AjaxResult.error("创建话题失败，消息已删除");
        }

        String propStr = message.getPropsStr();

        MessageProp messageProp = JSON.parseObject(StringUtils.defaultString(propStr, "{}"), MessageProp.class);
        messageProp.setTopicFlag(Boolean.TRUE);
        message.setPropsStr(JSON.toJSONString(messageProp, SerializerFeature.WriteClassName));

        messageMapper.updateMessage(message);

        messagePushService.pushCreateTopicEvent(msgId, message.getChannelId());

        return AjaxResult.success();
    }


    @Override
    public AjaxResult markTop(Long msgId) {

        LoginUser loginUser = SecurityUtils.getLoginUser();

        if (loginUser == null) {
            return AjaxResult.error("置顶消息失败：无法获取用户信息");
        }


        Message message = messageMapper.selectMessageById(msgId);
        if (message.getDeleteTime() > -1L) {
            return AjaxResult.error("置顶消息失败：消息已删除");
        }
        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>(ChannelMember.class);
        memberQueryWrapper.eq(ChannelMember::getChannelId, message.getChannelId());
        memberQueryWrapper.eq(ChannelMember::getUserId, loginUser.getUserid());
        List<ChannelMember> members = channelMemberMapper.selectList(memberQueryWrapper);
        if (CollectionUtils.isEmpty(members)) {
            return AjaxResult.error("置顶消息失败，您没有权限，请联系群主或群管理员");
        }
        ChannelMember member = members.remove(0);

        Long channelId = message.getChannelId();
        ChatChannel channel = chatChannelMapper.selectChatChannelById(channelId);
        if (channel == null) {
            return AjaxResult.error("置顶消息失败：群组不存在");
        }

        if (channel.getDeleteTime() > -1L) {
            return AjaxResult.error("置顶消息失败：群组已解散");
        }

        LambdaQueryWrapper<MsgTop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MsgTop::getChannelId, channelId);
        msgTopService.remove(queryWrapper);
        MsgTop msgTop = new MsgTop();
        msgTop.setId(SnowflakeIdUtil.nextId());
        msgTop.setChannelId(channelId);
        msgTop.setMsgId(msgId);
        msgTop.setCloseUsers(JSON.toJSONString(new ArrayList()));
        msgTop.setUserId(SecurityUtils.getUserId());
        msgTop.setCreateBy(msgTop.getUserId());
        msgTopService.save(msgTop);

        List<Message> messages = new ArrayList<>();
        messages.add(message);
        List<Long> messageIds = new ArrayList<>();
        messageIds.add(message.getId());
        List<MessageVo> messageVos = messageQueryService.processMetaData(messages, messageIds, null, null, null);

        MessageVo messageVo = messageVos.get(0);

        String userNickName = userNickNameService.getNickName(message.getUserId());
        messageVo.setCreateUser(userNickName);


        MsgTopVo msgTopVo = new MsgTopVo();
        BeanUtils.copyBeanProp(msgTopVo, msgTop);
        msgTopVo.setMessageData(messageVo);
        userNickName = userNickNameService.getNickName(msgTopVo.getUserId());
        msgTopVo.setCreateUser(userNickName);
        messagePushService.pushMessageTopEvent(msgTopVo);

        return AjaxResult.success(msgTop);
    }


    @Override
    public AjaxResult cancelTop(Long msgTopId) {

        LoginUser loginUser = SecurityUtils.getLoginUser();

        if (loginUser == null) {
            return AjaxResult.error("取消置顶消息失败：无法获取用户信息");
        }


        MsgTop msgTop = msgTopService.getById(msgTopId);
        if (msgTop == null) {
            return AjaxResult.success("置顶消息不存在");
        }
        msgTopService.removeById(msgTop.getId());
        Message message = messageMapper.selectMessageById(msgTop.getMsgId());


        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>(ChannelMember.class);
        memberQueryWrapper.eq(ChannelMember::getChannelId, message.getChannelId());
        memberQueryWrapper.eq(ChannelMember::getUserId, loginUser.getUserid());
        List<ChannelMember> members = channelMemberMapper.selectList(memberQueryWrapper);
        if (CollectionUtils.isEmpty(members)) {
            return AjaxResult.error("取消置顶消息失败，您没有权限，请联系群主或群管理员");
        }
        MessageVo messageVo = new MessageVo();
        BeanUtils.copyBeanProp(messageVo, message);

        List<MessageVo> messageVos = new ArrayList<>();
        messageVos.add(messageVo);
        messageQueryService.processMessageFileInfo(messageVos);
        String userNickName = userNickNameService.getNickName(message.getUserId());
        messageVo.setCreateUser(userNickName);

        MsgTopVo msgTopVo = new MsgTopVo();
        BeanUtils.copyBeanProp(msgTopVo, msgTop);
        msgTopVo.setMessageData(messageVo);
        userNickName = userNickNameService.getNickName(msgTopVo.getUserId());
        msgTopVo.setCreateUser(userNickName);

        messagePushService.pushMessageCancelTopEvent(msgTopVo);
        return AjaxResult.success(msgTop);
    }


    @Override
    public AjaxResult markTag(Long msgId) {
        return AjaxResult.error("暂不支持");
    }



    @Override
    public AjaxResult unmarkTag(Long msgId) {
        return AjaxResult.error("暂不支持");
    }



    public AjaxResult closeTop(Long msgId) {
        return AjaxResult.error("暂不支持关闭置顶功能");
    }

    private void processMessageRead(Message messageInDb, Long userId, String token) {

        LoginUser currentUser = AuthUtil.getLoginUser(token);
        SecurityContextHolder.set(SecurityConstants.LOGIN_USER, currentUser);

        MessageReadVo messageReadVo = new MessageReadVo();
        BeanUtils.copyBeanProp(messageReadVo, messageInDb);

        messageReadVo.setReadBy(userId);
        MessageMetadata metadata = messageReadVo.getMetaData();
        if (metadata == null) {
            metadata = new MessageMetadata();
            messageReadVo.setMetaData(metadata);
        }

        String propStr = messageInDb.getPropsStr();
        MessageProp prop = JSON.parseObject(propStr, MessageProp.class);
        if (prop.getForwardFlag() != null && prop.getForwardFlag()) {
            parseForwardMessage(prop.getForwardMsgIds(), messageReadVo, token);
        }

        List<MessageVo> messageReadVos = new ArrayList<>();
        MessageVo vo = new MessageVo();
        vo.setId(messageInDb.getId());
        vo.setMetaData(messageReadVo.getMetaData());
        messageReadVos.add(vo);
        messageQueryService.processTextTagDatas(messageReadVos);

        Long messageUserId = messageInDb.getUserId();

        messageReadVo.setCreateUser(userNickNameService.getNickName(messageUserId));


        reactionService.processReactionData(messageReadVo);


        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(userId);
        messageQueryService.processCardData(prop, metadata, loginUser);

        messageQueryService.processMessageFileInfo(messageReadVo);

        ChatBroadcast<Long, Long, ChannelId> messageBroadcast = new ChatBroadcast();
        messageBroadcast.setUserId(messageUserId);


        MessageReadEvent messageReadEvent = new MessageReadEvent();
        messageReadEvent.setData(messageReadVo);

        MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);
        statusFlag.setReadFlag(Boolean.TRUE);
        messageReadEvent.setBroadcast(messageBroadcast);
        chatEventService.doProcessEvent(messageReadEvent);


        statusFlag.setReadFlag(Boolean.TRUE);
        messageBroadcast = new ChatBroadcast();
        messageBroadcast.setUserId(userId);
        messageReadEvent.setToken(token);
        messageReadEvent.setUserId(userId);
        messageReadEvent.setBroadcast(messageBroadcast);
        chatEventService.doProcessEvent(messageReadEvent);

    }

    protected void parseForwardMessage(List<Long> forwardMsgIds, MessageReadVo messageVo, String token) {
        MessageMetadata metaData = messageVo.getMetaData();
        MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metaData);
        statusFlag.setForwardFlag(Boolean.TRUE);
        Long messageId = messageVo.getId();
        LambdaQueryWrapper<MsgForward> msgForwardQueryWrapper = new LambdaQueryWrapper<>();
        msgForwardQueryWrapper.select(MsgForward::getForwardMsgId, MsgForward::getDeletedFlag);
        msgForwardQueryWrapper.eq(MsgForward::getMsgId, messageId);
        msgForwardQueryWrapper.eq(MsgForward::getDeletedFlag, DELETE_FLAG_TRUE);
        List<MessageVo> forwardMessageVos = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(forwardMsgIds)) {
            List<Message> forwardMessages = new ArrayList<>();
            LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>(Message.class);
            messageQueryWrapper.in(Message::getId, forwardMsgIds);
            forwardMessages = messageMapper.selectList(messageQueryWrapper);
            forwardMessageVos = messageQueryService.processMetaDataForForward(forwardMessages, null, null, token);
            messageQueryService.processMessageFileInfo(forwardMessageVos);
        }
        metaData.setMsgForwardData(forwardMessageVos);
    }



    @Override
    public AjaxResult<List<MessageVo>> selectChannelMessageList(MessageQueryParam message) {
        return messageQueryService.selectChannelMessageList(message);
    }


    @Override
    public AjaxResult<List<MessageVo>> selectMessageList(MessageQueryParam message) {
        return messageQueryService.selectMessageList(message);
    }


    @Override
    public AjaxResult<List<Long>> selectMentionMessageIdList(MessageMentionQueryParam message) {
        return messageQueryService.selectMentionMessageIdList(message);
    }


    @Override
    public AjaxResult<List<MessageVo>> selectUserChannelMessageList(UserMessageQueryParam param) {
        return messageQueryService.selectUserChannelMessageList(param);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult<MessageVo> createMessage(MessageAddParam message) {

        Boolean exist = checkClientMsgExist(message);
        if (exist) {
            return AjaxResult.success("消息发送成功");
        }

        Long messageId = message.getId();
        if (message.getId() == null) {
            messageId = SnowflakeIdUtil.nextId();
            message.setId(messageId);
        }

        ChatChannel channel = chatChannelMapper.selectChatChannelById(message.getChannelId());
        String extDataString = channel.getExtData();
        ChannelExtData channelExtData = JSON.parseObject(StringUtils.defaultString(extDataString, "{}"), ChannelExtData.class);
        BanConfig banConfig = channelExtData.getBanConfig();

        LoginUser user = AuthUtil.getLoginUser();
        if (user != null && banConfig != null) {
            checkBanConfig(user, banConfig, channel);
        }

        MessageProp prop = message.getProps();
        if (prop == null) {
            prop = new MessageProp();
            message.setProps(prop);
        }
        if (prop.getFromBot() != null && prop.getFromBot()) {
            user = new LoginUser();
            user.setUserid(message.getUserId());
        }

        if (user == null || user.getUserid() == null) {
            return AjaxResult.error("发送消息失败，无法获取用户信息");
        }
        prop.setReplyFlag(message.getReplyMsgId() != null);
        if (message.getReplyMsgId() != null) {
            prop.setReplyMsgId(message.getReplyMsgId());
        }

        if (prop != null) {
            message.setPropsStr(JSON.toJSONString(prop, SerializerFeature.WriteClassName));
        }

        message.setUserId(user.getUserid());

        List<Long> mentionUserList = message.getMentionUserList();
        if (mentionUserList == null) {
            mentionUserList = new ArrayList<>();
        }
        if (message.getMentionAll() != null && message.getMentionAll()) {
            mentionUserList.clear();
            mentionUserList.add(-1L);
        }

        message.setMentionUsers(JSON.toJSONString(mentionUserList));


        List<Long> fileIds = message.getFileIds();
        if (fileIds == null) {
            fileIds = new ArrayList<>();
        }
        List<MsgFile> messageFiles = null;
        if (CollectionUtils.isNotEmpty(fileIds)) {
            LambdaQueryWrapper<MsgFile> msgFileQueryWrapper = new LambdaQueryWrapper(MsgFile.class);
            msgFileQueryWrapper.select(MsgFile::getId, MsgFile::getName).in(MsgFile::getId, fileIds);
            messageFiles = msgFileMapper.selectList(msgFileQueryWrapper);


            LambdaUpdateWrapper<MsgFile> updateFileWrapper = new LambdaUpdateWrapper<>();
            updateFileWrapper.set(MsgFile::getMsgId, messageId);
            updateFileWrapper.in(MsgFile::getId, fileIds);
            updateFileWrapper.isNull(MsgFile::getMsgId);
            msgFileMapper.update(null, updateFileWrapper);
        }

        if (messageFiles == null) {
            messageFiles = new ArrayList<>();
        }
        if (CollectionUtils.isNotEmpty(messageFiles)) {
            messageFiles.forEach(item -> {
                item.setMsgId(null);
            });
        }
        message.setMsgFiles(JSON.toJSONString(messageFiles));

        message.setCreateTime(new Date());
        message.setCreateBy(user.getUserid());

        String extData = message.getMsgExtData();
        if (StringUtils.isNotEmpty(extData)) {
            processExtData(extData, message);
        }

        messageMapper.insertMessage(message);
        message.setCreateUser(userNickNameService.getNickName(message.getUserId()));

        List<MsgUnread> unreadList = new ArrayList<>();
        MsgStatistic stastic = new MsgStatistic();
        stastic.setMsgId(messageId);
        stastic.setUserId(user.getUserid());


        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>();
        memberQueryWrapper.select(ChannelMember::getId, ChannelMember::getUserId, ChannelMember::getUnreadCount, ChannelMember::getRootMsgCount, ChannelMember::getMentionCount);
        memberQueryWrapper.eq(ChannelMember::getChannelId, message.getChannelId());
        memberQueryWrapper.ne(ChannelMember::getUserId, user.getUserid());
        List<ChannelMember> channelMembers = channelMemberMapper.selectList(memberQueryWrapper);

        Integer memberCount = 0;
        if (CollectionUtils.isNotEmpty(channelMembers)) {
            memberCount = channelMembers.size();


            unreadList = new ArrayList<>(channelMembers.size());
            List<MsgUnread> finalUnreadList = unreadList;
            Long finalMessageId = messageId;
            channelMembers.forEach(member -> {
                MsgUnread unread = new MsgUnread();
                unread.setId(SnowflakeIdUtil.nextId());
                unread.setMsgId(finalMessageId);
                unread.setChannelId(message.getChannelId());
                unread.setUserId(member.getUserId());
                finalUnreadList.add(unread);
            });
            msgUnreadMapper.batchInsert(unreadList);
        }
        stastic.setUnreadCount(memberCount);
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

        processChannelUnread(channel, totalMsg, channelMembers, mentionUserList, message.getRootId() == null);

        statisticMapper.insertMsgStatistic(stastic);

        processUnAckInfo(message, channelMembers, messageId, stastic);


        if (message.getRootId() != null) {
            msgReplyService.processReplyMessage(message);
        }

        message.setDeleteTime(-1L);
        processPushMessage(message, new ArrayList<>(), new ArrayList<>());
        return AjaxResult.success(message);
    }

    private void checkBanConfig(LoginUser user, BanConfig banConfig, ChatChannel channel) {

        Long userId = user.getUserid();
        if (userId == null) {
            return;
        }

        if (banConfig.getBanAllFlag() != null && banConfig.getBanAllFlag()) {
            List<Long> whiteList = banConfig.getWhitelist();
            LambdaQueryWrapper<ChannelMember> memberLambdaQueryWrapper = new LambdaQueryWrapper<>(ChannelMember.class);
            memberLambdaQueryWrapper.eq(ChannelMember::getChannelId, channel.getId());
            memberLambdaQueryWrapper.eq(ChannelMember::getUserId, user.getUserid());
            List<ChannelMember> members = channelMemberMapper.selectList(memberLambdaQueryWrapper);
            if (CollectionUtils.isNotEmpty(members)) {
                ChannelMember member = members.remove(0);
                Integer memberRole = member.getManager();
                if (MEMBER_ROLE_OWNER.equals(memberRole) || MEMBER_ROLE_MANAGER.equals(memberRole)) {
                    return;
                }
            }

            if (CollectionUtils.isEmpty(whiteList) || !whiteList.contains(userId)) {
                throw new BaseException("发送消息失败，用户被禁言，请联系群主或群管理员");
            }
        } else {
            List<Long> blacklist = banConfig.getBlacklist();
            if (CollectionUtils.isNotEmpty(blacklist) && blacklist.contains(userId)) {
                throw new BaseException("发送消息失败，用户被禁言，请联系群主或群管理员");
            }
        }

    }


    private void processExtData(String extData, MessageAddParam message) {

    }

    private Boolean checkClientMsgExist(MessageAddParam message) {
        Boolean result = Boolean.FALSE;
        if (StringUtils.isNotEmpty(message.getUuid())) {
            LambdaQueryWrapper<Message> msgQueryWrapper = new LambdaQueryWrapper<>();
            msgQueryWrapper.select(Message::getId);
            msgQueryWrapper.eq(Message::getUuid, message.getUuid());
            Message msgData = messageMapper.selectOne(msgQueryWrapper);
            result = msgData != null;
        }
        return result;
    }

    private void processChannelUnread(ChatChannel channel, Long totalMsg, List<ChannelMember> channelMembers, List<Long> mentionUserList, boolean rootFlag) {
        if (CHANNEL_TYPE_DIRECT.equals(channel.getType())) {
            channelMembers.forEach(member -> {
                LambdaUpdateWrapper<ChannelMember> memberUpdateWrapper = new LambdaUpdateWrapper<>();
                memberUpdateWrapper.set(ChannelMember::getMsgCount, totalMsg);
                if (rootFlag) {
                    memberUpdateWrapper.set(ChannelMember::getRootMsgCount, member.getRootMsgCount() + 1);
                }
                memberUpdateWrapper.set(ChannelMember::getUnreadCount, member.getUnreadCount() + 1);

                memberUpdateWrapper.eq(ChannelMember::getId, member.getId());
                channelMemberMapper.update(null, memberUpdateWrapper);
            });
        } else {
            channelMembers.forEach(member -> {
                LambdaUpdateWrapper<ChannelMember> memberUpdateWrapper = new LambdaUpdateWrapper<>();
                memberUpdateWrapper.set(ChannelMember::getMsgCount, totalMsg);
                if (rootFlag) {

                    memberUpdateWrapper.set(ChannelMember::getRootMsgCount, member.getRootMsgCount() + 1);
                }
                memberUpdateWrapper.set(ChannelMember::getUnreadCount, member.getUnreadCount() + 1);
                if (mentionUserList.contains(member.getUserId())) {
                    memberUpdateWrapper.set(ChannelMember::getMentionCount, member.getMentionCount() + 1);
                }
                memberUpdateWrapper.eq(ChannelMember::getId, member.getId());
                channelMemberMapper.update(null, memberUpdateWrapper);
            });
        }
    }



    @Transactional(rollbackFor = Exception.class)
    public <Data extends SystemMessageData> AjaxResult<MessageVo> createSystemMessage(MessageAddParam message, Data messageData) {
        Long messageId = message.getId();
        if (messageId == null) {
            messageId = SnowflakeIdUtil.nextId();
            message.setId(messageId);
        }
        LoginUser user = AuthUtil.getLoginUser();

        MessageProp prop = message.getProps();
        if (prop == null) {
            prop = new MessageProp();
        }
        if (prop.getFromBot() != null && prop.getFromBot()) {
            user = new LoginUser();
            user.setUserid(message.getUserId());
        }

        SystemMessageData systemMessageData = prop.getSystemData();
        if (systemMessageData instanceof RecallMessageData) {
            Message deletedMessage = ((RecallMessageData) systemMessageData).getDeletedMessage();
            ((RecallMessageData) systemMessageData).setDeletedMessage(null);
            message.setPropsStr(JSON.toJSONString(prop, SerializerFeature.WriteClassName));
            ((RecallMessageData) systemMessageData).setDeletedMessage(deletedMessage);
        } else {
            message.setPropsStr(JSON.toJSONString(prop, SerializerFeature.WriteClassName));
        }

        message.setUserId(user.getUserid());

        List<Long> mentionUserList = message.getMentionUserList();
        if (mentionUserList == null) {
            mentionUserList = new ArrayList<>();
        }
        if (message.getMentionAll() != null && message.getMentionAll()) {
            mentionUserList.clear();
            mentionUserList.add(-1L);
        }

        message.setMentionUsers(JSON.toJSONString(mentionUserList));


        List<Long> fileIds = message.getFileIds();
        if (fileIds == null) {
            fileIds = new ArrayList<>();
        }
        List<MsgFile> messageFiles = null;
        if (CollectionUtils.isNotEmpty(fileIds)) {
            LambdaQueryWrapper<MsgFile> msgFileQueryWrapper = new LambdaQueryWrapper(MsgFile.class);
            msgFileQueryWrapper.select(MsgFile::getId, MsgFile::getName).in(MsgFile::getId, fileIds);
            messageFiles = msgFileMapper.selectList(msgFileQueryWrapper);


            LambdaUpdateWrapper<MsgFile> updateFileWrapper = new LambdaUpdateWrapper<>();
            updateFileWrapper.set(MsgFile::getMsgId, messageId);
            updateFileWrapper.in(MsgFile::getId, fileIds);
            updateFileWrapper.isNull(MsgFile::getMsgId);
            msgFileMapper.update(null, updateFileWrapper);
        }

        if (messageFiles == null) {
            messageFiles = new ArrayList<>();
        }
        if (CollectionUtils.isNotEmpty(messageFiles)) {
            messageFiles.forEach(item -> {
                item.setMsgId(null);
            });
        }
        message.setMsgFiles(JSON.toJSONString(messageFiles));
        messageMapper.insertMessage(message);


        if (message.getRootId() != null) {
            msgReplyService.processReplyMessage(message);
        }

        if (messageData instanceof RecallMessageData) {
            msgUnreadService.processRecallMessageUnread((RecallMessageData) messageData);
        }


        message.setDeleteTime(-1L);

        processPushMessage(message, new ArrayList<>(), new ArrayList<>());
        return AjaxResult.success(message);
    }


    @Override
    public void generateSystemMessage(SystemMessage systemMessage, Long messageId) {
        MessageAddParam message = new MessageAddParam();
        SystemMessageData messageData = systemMessage.getData();
        if (messageData instanceof RecallMessageData) {
            RecallMessageData deleteMessageData = (RecallMessageData) messageData;
            Message deletedMessage = deleteMessageData.getDeletedMessage();
            BeanUtils.copyBeanProp(message, deletedMessage);
        }

        message.setCreateBy(systemMessage.getUserId());
        message.setId(systemMessage.getMessageId());
        if (message.getId() == null) {
            if (messageId == null) {
                message.setId(SnowflakeIdUtil.nextId());
            } else {
                message.setId(messageId);
            }
        }
        message.setUserId(systemMessage.getUserId());
        message.setOriginalId(systemMessage.getOriginalId());
        String content = systemMessage.getData().getContent();
        systemMessage.getData().setContent(null);
        message.setContent(content);
        message.setType(MESSAGE_TYPE_SYSTEM);
        message.setChannelId(systemMessage.getChannelId());

        MessageProp<SystemMessageData> messageProp = new MessageProp<>();
        messageProp.setSystemData(messageData);
        message.setProps(messageProp);

        createSystemMessage(message, systemMessage.getData());
    }


    private void processMentionForRead(Long userId, List<Long> mentionUsers, ChannelMember member, boolean isRoot) {
        if (CollectionUtils.isNotEmpty(mentionUsers)) {
            Long mentionCount = null;
            if (mentionUsers.contains(userId)) {

                member.getMentionCount();
                if (mentionCount == null) {
                    mentionCount = 1L;
                }
                if (mentionCount > 0L) {
                    mentionCount--;
                }
            } else if (mentionUsers.size() == 1 && mentionUsers.get(0) == null || Long.valueOf(-1L).equals(mentionUsers.get(0))) {

                member.getMentionCount();
                if (mentionCount == null) {
                    mentionCount = 1L;
                }
                if (mentionCount > 0L) {
                    mentionCount--;
                }
            }

            if (mentionCount == null) {
                mentionCount = 0L;
            }

            member.setMentionCount(mentionCount);

            if (isRoot) {
                Long mentionRootCount = member.getMentionRootCount();
                if (mentionRootCount == null) {
                    mentionRootCount = 1L;
                }

                if (mentionRootCount > 0L) {
                    mentionRootCount--;
                }
                member.setMentionRootCount(mentionRootCount);
            }
        }
    }


    private void processPushMessage(MessageAddParam message, List<MsgUnread> unreadList, List<MsgAck> unAckList) {

        MessagePostedEvent event = new MessagePostedEvent();
        MessageVo messageVo = new MessageVo();
        BeanUtils.copyBeanProp(messageVo, message);
        messageVo.setPropsStr(null);

        MessageMetadata messageMetadata = messageVo.getMetaData();
        if (messageMetadata == null) {
            messageMetadata = new MessageMetadata();
            messageVo.setMetaData(messageMetadata);
        }
        List<MessageVo> messageVos = new ArrayList<>();
        messageVos.add(messageVo);
        messageQueryService.processTextTagDatas(messageVos);
        MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(messageMetadata);
        MetaCount metaCount = MessageMetaUtil.getCount(messageMetadata);

        statusFlag.setReadFlag(Boolean.FALSE);


        metaCount.setUnAckCount(unAckList.size());
        messageMetadata.setUnAckUserList(unAckList);
        messageMetadata.setMentionUsers(unAckList.stream().map(MsgAck::getUserId).collect(Collectors.toList()));


        messageQueryService.processCardData(message.getProps(), messageMetadata, null);
        MessageProp prop = message.getProps();
        if (prop != null) {
            if (StringUtils.isNotEmpty(prop.getContentType())) {
                messageMetadata.setContentType(prop.getContentType());
            }
        }
        event.setData(messageVo);


        ChatBroadcast<Long, Long, ChannelId> messageBroadcast = new ChatBroadcast();
        messageBroadcast.setChannelId(message.getChannelId());

        if (prop != null) {
            processBroadcastByProp(prop, messageBroadcast);
            if (prop.getReplyFlag() != null) {
                statusFlag.setReplyFlag(prop.getReplyFlag());
            } else {
                statusFlag.setReplyFlag(Boolean.FALSE);
            }
            messageMetadata.setReplyMsgId(prop.getReplyMsgId());
            SystemMessageData systemData = prop.getSystemData();
            if (systemData != null) {
                metaCount.setUnreadCount(0);
                messageMetadata.setUnreadUserList(unreadList);
                statusFlag.setReadFlag(Boolean.TRUE);
                messageMetadata.setSystemData(systemData);
            }
            if (StringUtils.isNotEmpty(prop.getContentType())) {
                messageMetadata.setContentType(prop.getContentType());
            }
        }
        processRefReplyMessage(messageMetadata);

        event.setBroadcast(messageBroadcast);

        chatEventService.doProcessEvent(event);
    }


    private void processRefReplyMessage(MessageMetadata metadata) {
        Long replyMsgId = metadata.getReplyMsgId();
        Message refMessage = null;
        if (replyMsgId != null) {
            refMessage = messageMapper.selectMessageById(metadata.getReplyMsgId());
        }
        if (refMessage != null) {
            MessageVo messageVo = new MessageVo();
            BeanUtils.copyProperties(refMessage, messageVo);
            metadata.setReplyMsgData(messageVo);

            MessageMetadata replyMetadata = messageVo.getMetaData();
            if (replyMetadata == null) {
                replyMetadata = new MessageMetadata();
                messageVo.setMetaData(replyMetadata);
            }
            processReplyMsgMetadata(messageVo);
        }
    }

    private void processReplyMsgMetadata(MessageVo messageVo) {

        reactionService.processReactionData(messageVo);

        MessageMetadata metadata = messageVo.getMetaData();
        if (metadata == null) {
            metadata = new MessageMetadata();
            messageVo.setMetaData(metadata);
        }

        MessageProp messageProp = JSON.parseObject(StringUtils.defaultString(messageVo.getPropsStr(), "{}"), MessageProp.class);

        MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);
        if (messageProp.getForwardFlag() != null) {
            statusFlag.setForwardFlag(messageProp.getForwardFlag());
        }

        LambdaQueryWrapper<MsgFile> msgFileQueryWrapper = new LambdaQueryWrapper<>();
        msgFileQueryWrapper.select(MsgFile::getMsgId, MsgFile::getName, MsgFile::getId, MsgFile::getSize, MsgFile::getExternalId,
                MsgFile::getExtension, MsgFile::getHasPreviewImage, MsgFile::getWidth, MsgFile::getHeight, MsgFile::getMiniPreview);
        msgFileQueryWrapper.eq(MsgFile::getMsgId, messageVo.getId());
        msgFileQueryWrapper.eq(MsgFile::getDeleteTime, -1L);
        List<MsgFile> msgFileList = msgFileMapper.selectList(msgFileQueryWrapper);

        messageVo.setFiles(msgFileList);
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



    private void processMessageDelete(Message message, Long userId, Long messageId) {
        if (message == null) {
            log.warn("生成删除消息事件失败，消息为null");
            return;
        }
        SystemMessage<RecallMessageData> deleteMessage = new SystemMessage<>();
        deleteMessage.setChannelId(message.getChannelId());


        RecallMessageData deleteData = new RecallMessageData();
        deleteData.setDeleteBy(userId);
        String realName = userNickNameService.getNickName(userId);

        deleteData.setDeleteByUser(realName);
        deleteData.setDeletedMessage(message);

        deleteMessage.setData(deleteData);
        deleteMessage.setOriginalId(messageId);
        deleteMessage.setMessageId(message.getId());

        generateSystemMessage(deleteMessage, null);
    }

    private List<MsgAck> processUnAckInfo(MessageAddParam message, List<ChannelMember> channelMembers, Long messageId, MsgStatistic stastic) {
        if (message.getRequestedAck() != null && message.getRequestedAck().equals(Short.valueOf("1"))) {
            message.setMentionAll(Boolean.TRUE);
        } else {
            message.setMentionAll(Boolean.FALSE);
        }

        List<MsgAck> unAckList;
        if (message.getMentionAll()) {
            unAckList = new ArrayList<>(channelMembers.size());
            channelMembers.forEach(member -> {
                MsgAck msgAck = new MsgAck();
                msgAck.setId(SnowflakeIdUtil.nextId());
                msgAck.setMsgId(messageId);
                msgAck.setChannelId(member.getChannelId());
                msgAck.setUserId(member.getUserId());
                unAckList.add(msgAck);
            });
        } else {
            if (CollectionUtils.isNotEmpty(message.getMentionUserList())) {
                unAckList = new ArrayList<>(message.getMentionUserList().size());
                message.getMentionUserList().forEach(userId -> {
                    MsgAck msgAck = new MsgAck();
                    msgAck.setId(SnowflakeIdUtil.nextId());
                    msgAck.setMsgId(messageId);
                    msgAck.setChannelId(message.getChannelId());
                    msgAck.setUserId(userId);
                    unAckList.add(msgAck);
                });
            } else {
                unAckList = new ArrayList<>();
            }
        }
        if (CollectionUtils.isNotEmpty(unAckList)) {
            msgAckMapper.batchInsert(unAckList);
        }
        stastic.setUnAckCount(unAckList.size());
        return unAckList;
    }


    private Map<Long, String> getUserIdNameMap(List<Message> messages, Map<Long, String> userIdMap, Map<Long, String> botIdMap) {

        Map<Long, String> result = new HashMap<>();
        Set<Long> userIds = new HashSet<>();
        Set<Long> botIds = new HashSet<>();
        messages.stream().forEach(item -> {
            if (MESSAGE_TYPE_USER.equals(item.getType())) {
                userIds.add(item.getUserId());
            } else if (MESSAGE_TYPE_BOT.equals(item.getType())) {
                botIds.add(item.getUserId());
            }
        });


        if (CollectionUtils.isNotEmpty(userIds)) {
            Map<Long, String> userIdNameMap = userNickNameService.getNickNameByIds(new ArrayList<>(userIds));
            if (MapUtils.isNotEmpty(userIdNameMap)) {
                result.putAll(userIdNameMap);
                userIdMap.putAll(userIdNameMap);
            }
        }

        return result;
    }



    protected void processMessageFileInfo(List<MessageVo> msgList) {
        List<Long> msgIds = new ArrayList<>();
        msgList.forEach(item -> {
            msgIds.add(item.getId());
        });

        LambdaQueryWrapper<MsgFile> msgFileQueryWrapper = new LambdaQueryWrapper<>();
        msgFileQueryWrapper.select(MsgFile::getMsgId, MsgFile::getName, MsgFile::getId, MsgFile::getSize, MsgFile::getExternalId,
                MsgFile::getExtension, MsgFile::getHasPreviewImage, MsgFile::getWidth, MsgFile::getHeight, MsgFile::getMiniPreview);
        msgFileQueryWrapper.in(MsgFile::getMsgId, msgIds);
        msgFileQueryWrapper.eq(MsgFile::getDeleteTime, -1L);
        List<MsgFile> msgFileList = msgFileMapper.selectList(msgFileQueryWrapper);
        Map<Long, List<MsgFile>> msgFiles = msgFileList.stream().map(item -> {
            if (StringUtils.isNotEmpty(item.getExtension())) {
                String name = item.getName();
                name = name + "." + item.getExtension();
                item.setName(name);
            }
            return item;
        }).collect(Collectors.groupingBy(MsgFile::getMsgId));
        msgList.forEach(msg -> {
            msg.setFiles(msgFiles.get(msg.getId()));
        });
    }

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Autowired
    public void setMsgUnreadMapper(MsgUnreadMapper msgUnreadMapper) {
        this.msgUnreadMapper = msgUnreadMapper;
    }

    @Autowired
    public void setStatisticMapper(MsgStatisticMapper statisticMapper) {
        this.statisticMapper = statisticMapper;
    }

    @Autowired
    public void setMsgAckMapper(MsgAckMapper msgAckMapper) {
        this.msgAckMapper = msgAckMapper;
    }

    @Autowired
    public void setChannelMemberMapper(ChannelMemberMapper channelMemberMapper) {
        this.channelMemberMapper = channelMemberMapper;
    }

    @Autowired
    public void setChatEventService(ChatEventService chatEventService) {
        this.chatEventService = chatEventService;
    }

    @Autowired
    public void setChatChannelMapper(ChatChannelMapper chatChannelMapper) {
        this.chatChannelMapper = chatChannelMapper;
    }

    @Autowired
    public void setMsgPinnedService(IMsgPinnedService msgPinnedService) {
        this.msgPinnedService = msgPinnedService;
    }

    @Autowired
    public void setUserNickNameService(UserNickNameService userNickNameService) {
        this.userNickNameService = userNickNameService;
    }

    @Autowired
    public void setMsgReplyService(IMsgReplyService msgReplyService) {
        this.msgReplyService = msgReplyService;
    }

    @Autowired
    public void setMessageQueryService(MessageQueryService messageQueryService) {
        this.messageQueryService = messageQueryService;
    }

    @Autowired
    public void setMsgUnreadService(IMsgUnreadService msgUnreadService) {
        this.msgUnreadService = msgUnreadService;
    }

    @Autowired
    public void setMessagePushService(MessagePushService messagePushService) {
        this.messagePushService = messagePushService;
    }

    @Autowired
    public void setMsgTopService(IMsgTopService msgTopService) {
        this.msgTopService = msgTopService;
    }

    @Autowired
    public void setMsgFileMapper(MsgFileMapper msgFileMapper) {
        this.msgFileMapper = msgFileMapper;
    }

    @Autowired
    public void setReactionService(IMsgReactionService reactionService) {
        this.reactionService = reactionService;
    }
}
