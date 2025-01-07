package com.soflyit.chattask.im.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.domain.param.ChannelAddParam;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.channel.mapper.ChatChannelMapper;
import com.soflyit.chattask.im.channel.service.IChatChannelService;
import com.soflyit.chattask.im.common.util.MessageMetaUtil;
import com.soflyit.chattask.im.event.domain.MessagePostedEvent;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.im.message.domain.entity.*;
import com.soflyit.chattask.im.message.domain.param.ForwardChannelInfo;
import com.soflyit.chattask.im.message.domain.param.MessageAddParam;
import com.soflyit.chattask.im.message.domain.param.MessageForwardParam;
import com.soflyit.chattask.im.message.domain.vo.*;
import com.soflyit.chattask.im.message.mapper.*;
import com.soflyit.chattask.im.message.service.IMessageService;
import com.soflyit.chattask.im.message.service.IMsgForwardService;
import com.soflyit.chattask.im.message.service.IMsgReactionService;
import com.soflyit.chattask.im.system.service.impl.UserNickNameService;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.common.core.constant.SecurityConstants;
import com.soflyit.common.core.context.SecurityContextHolder;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.soflyit.chattask.im.common.constant.ChannelConstant.CHANNEL_TYPE_DIRECT;
import static com.soflyit.chattask.im.common.constant.MessageConstant.*;

/**
 * 消息转发Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Slf4j
@Service
public class MsgForwardServiceImpl implements IMsgForwardService {

    private MsgForwardMapper msgForwardMapper;

    private MessageMapper messageMapper;

    private ChatChannelMapper chatChannelMapper;

    private IChatChannelService chatChannelService;

    private ChannelMemberMapper channelMemberMapper;

    private MsgUnreadMapper msgUnreadMapper;

    private MsgStatisticMapper msgStatisticMapper;

    private MsgAckMapper msgAckMapper;

    private TransactionTemplate transactionTemplate;

    private ChatEventService chatEventService;

    private UserNickNameService userNickNameService;

    private MsgReplyMapper msgReplyMapper;

    private MsgFileMapper msgFileMapper;
    private IMessageService messageService;

    @Autowired
    private IMsgReactionService msgReactionService;

    @Autowired
    private MessageQueryService messageQueryService;


    @Override
    public MsgForward selectMsgForwardById(Long id) {
        return msgForwardMapper.selectMsgForwardById(id);
    }


    @Override
    public List<MsgForward> selectMsgForwardList(MsgForward msgForward) {
        return msgForwardMapper.selectMsgForwardList(msgForward);
    }


    @Override
    public int insertMsgForward(MsgForward msgForward) {
        msgForward.setCreateTime(DateUtils.getNowDate());
        return msgForwardMapper.insertMsgForward(msgForward);
    }


    @Override
    public int updateMsgForward(MsgForward msgForward) {
        msgForward.setUpdateTime(DateUtils.getNowDate());
        return msgForwardMapper.updateMsgForward(msgForward);
    }


    @Override
    public int deleteMsgForwardByIds(Long[] ids) {
        return msgForwardMapper.deleteMsgForwardByIds(ids);
    }


    @Override
    public int deleteMsgForwardById(Long id) {
        return msgForwardMapper.deleteMsgForwardById(id);
    }


    @Override
    public AjaxResult forwardMessage(MessageForwardParam forwardParam) {

        Short forwardType = forwardParam.getForwardType();
        if (FORWARD_TYPE_SINGLE.equals(forwardType)) {
            return forwardSingleMessage(forwardParam);
        } else if (FORWARD_TYPE_MERGE.equals(forwardType)) {
            return forwardMergeMessage(forwardParam);
        } else {
            String msg = "消息转发失败，转发类型无法识别";
            return AjaxResult.error(msg);
        }
    }

    private AjaxResult forwardMergeMessage(MessageForwardParam forwardParam) {
        List<Long> msgIds = forwardParam.getForwardMsgIds();
        LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>(Message.class);
        messageQueryWrapper.in(Message::getId, msgIds);
        List<Message> messages = messageMapper.selectList(messageQueryWrapper);

        if (CollectionUtils.isEmpty(messages)) {
            log.error("转发消息失败：消息不存在：{}", msgIds);
            return AjaxResult.error("转发消息失败：消息不存在");
        }

        Map<Integer, List<ForwardChannelInfo>> channalMap = forwardParam.getChannelIds().stream().collect(Collectors.groupingBy(ForwardChannelInfo::getDataType));
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String realName = userNickNameService.getNickName(loginUser.getUserid());
        return transactionTemplate.execute(new TransactionCallback<AjaxResult>() {
            @Override
            public AjaxResult doInTransaction(TransactionStatus status) {
                try {
                    SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
                    SecurityContextHolder.setUserName(loginUser.getUsername());
                    SecurityContextHolder.setUserId(String.valueOf(loginUser.getUserid()));

                    List<Long> channelIds = new ArrayList<>();
                    if (MapUtils.isNotEmpty(channalMap)) {
                        List<ForwardChannelInfo> forwardChannelInfos = channalMap.get(FORWARD_CHANNEL_ID_TYPE_CHANNEL);
                        if (CollectionUtils.isNotEmpty(forwardChannelInfos)) {
                            forwardChannelInfos.stream().forEach(item -> channelIds.add(item.getDataId()));
                        }

                        List<ForwardChannelInfo> userIds = channalMap.get(FORWARD_CHANNEL_ID_TYPE_USER);
                        if (CollectionUtils.isNotEmpty(userIds)) {
                            parseUserIds(userIds, SecurityUtils.getLoginUser(), channelIds);
                        }

                    }
                    if (CollectionUtils.isEmpty(channelIds)) {
                        String msg = "消息转发失败，频道Id不能为空";
                        return AjaxResult.error(msg);
                    }

                    forwardMergeMessage(forwardParam.getContent(), messages, channelIds, realName, forwardParam.getReplyFlag());

                    return AjaxResult.success();
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error(e.getMessage(), e);
                    throw new BaseException(e.getMessage());
                }
            }

            private void forwardMergeMessage(String content, List<Message> messages, List<Long> channelIds, String realName, Boolean replyFlag) {

                List<Long> forwardMsgIds = messages.stream().map(Message::getId).collect(Collectors.toList());

                channelIds.forEach(channelId -> {

                    Message message = new Message();
                    message.setContent(content);
                    message.setUserId(loginUser.getUserid());
                    message.setChannelId(channelId);
                    message.setCreateBy(loginUser.getUserid());
                    message.setType(MESSAGE_TYPE_USER);
                    message.setId(SnowflakeIdUtil.nextId());
                    String propStr = message.getPropsStr();
                    MessageProp prop = JSON.parseObject(StringUtils.defaultIfBlank(propStr, "{}"), MessageProp.class);
                    prop.setForwardFlag(Boolean.TRUE);
                    prop.setForwardMsgIds(forwardMsgIds);
                    prop.setReplyFlag(replyFlag);

                    for (int i = 0; i < messages.size(); i++) {

                        MsgForward forward = new MsgForward();
                        forward.setId(SnowflakeIdUtil.nextId());
                        forward.setMsgId(message.getId());
                        forward.setForwardMsgId(messages.get(i).getId());
                        forward.setDeletedFlag(DELETE_FLAG_FALSE);
                        forward.setSortOrder(Integer.valueOf(i));
                        msgForwardMapper.insertMsgForward(forward);
                    }
                    if (replyFlag != null && replyFlag && forwardMsgIds.size() == 1) {
                        prop.setReplyMsgId(forwardMsgIds.get(0));
                    }
                    message.setPropsStr(JSON.toJSONString(prop, SerializerFeature.WriteClassName));
                    messageMapper.insertMessage(message);

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


                    processPushMessage(message, unreadList, unAckList, messages.stream().map(Message::getId).collect(Collectors.toList()), null);
                });
            }
        });

    }


    private AjaxResult forwardSingleMessage(MessageForwardParam forwardParam) {
        List<Long> msgIds = forwardParam.getForwardMsgIds();
        LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>(Message.class);
        messageQueryWrapper.in(Message::getId, msgIds);
        List<Message> messages = messageMapper.selectList(messageQueryWrapper);

        Map<Integer, List<ForwardChannelInfo>> channalMap = forwardParam.getChannelIds().stream().collect(Collectors.groupingBy(ForwardChannelInfo::getDataType));
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String realName = userNickNameService.getNickName(loginUser.getUserid());

        ForwardTransactionParam forwardTransactionParam = new ForwardTransactionParam();
        forwardTransactionParam.setMessages(messages);
        forwardTransactionParam.setForwardParam(forwardParam);
        forwardTransactionParam.setChannalMap(channalMap);
        forwardTransactionParam.setRealName(realName);
        forwardTransactionParam.setLoginUser(loginUser);

        ForwardSingleTransactionCallBack callback = new ForwardSingleTransactionCallBack(forwardTransactionParam);
        callback.setMsgForwardMapper(msgForwardMapper);
        callback.setMessageMapper(messageMapper);
        callback.setMsgForwardService(this);
        callback.setMsgFileMapper(msgFileMapper);
        callback.setChannelMemberMapper(channelMemberMapper);
        callback.setMsgStatisticMapper(msgStatisticMapper);
        callback.setMsgUnreadMapper(msgUnreadMapper);
        callback.setChatChannelMapper(chatChannelMapper);

        return transactionTemplate.execute(callback);
    }

    protected void generateNewMessage(MessageForwardParam forwardParam, LoginUser loginUser, List<Long> channelIds) {
        channelIds.forEach(channelId -> {
            MessageAddParam message = new MessageAddParam();
            message.setChannelId(channelId);
            message.setContent(forwardParam.getContent());
            message.setCreateTime(new Date());
            message.setType(MESSAGE_TYPE_USER);
            message.setCreateBy(loginUser.getUserid());
            message.setMentionUsers("[]");
            messageService.createMessage(message);
        });

    }



    protected void processPushMessage(Message message, List<MsgUnread> unreadList, List<MsgAck> unAckList, List<Long> forwardMsgIds, List<MsgFile> msgFileList) {

        MessagePostedEvent event = new MessagePostedEvent();
        MessageVo messageVo = new MessageVo();
        BeanUtils.copyBeanProp(messageVo, message);
        messageVo.setCreateUser(userNickNameService.getNickName(message.getCreateBy()));

        MessageMetadata messageMetadata = messageVo.getMetaData();
        if (messageMetadata == null) {
            messageMetadata = new MessageMetadata();
            messageVo.setMetaData(messageMetadata);
        }


        MetaCount metaCount = MessageMetaUtil.getCount(messageMetadata);
        metaCount.setUnAckCount(unAckList.size());
        messageMetadata.setUnAckUserList(unAckList);


        if (CollectionUtils.isNotEmpty(forwardMsgIds)) {
            parseForwardMessage(forwardMsgIds, messageVo);
        }
        if (CollectionUtils.isNotEmpty(msgFileList)) {
            parseForwardMsgFile(msgFileList, messageVo);
        }

        List<MessageVo> messageVos = new ArrayList<>();
        messageVos.add(messageVo);
        messageQueryService.processTextTagDatas(messageVos);

        String propStr = message.getPropsStr();
        if (StringUtils.isNotEmpty(propStr)) {
            MessageProp messageProp = JSON.parseObject(propStr, MessageProp.class);

            Boolean replyFlag = messageProp.getReplyFlag();
            if (replyFlag != null) {
                MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(messageMetadata);
                statusFlag.setReplyFlag(messageProp.getReplyFlag());
            }
            if (messageProp.getReplyMsgId() != null) {
                messageMetadata.setReplyMsgId(messageProp.getReplyMsgId());
            }
        }

        event.setData(messageVo);


        ChatBroadcast<Long, Long, ChannelId> messageBroadcast = new ChatBroadcast();
        messageBroadcast.setChannelId(message.getChannelId());

        event.setBroadcast(messageBroadcast);

        chatEventService.doProcessEvent(event);
    }

    private void parseForwardMsgFile(List<MsgFile> msgFileList, MessageVo messageVo) {

        List<MsgFile> files = JSON.parseArray(JSON.toJSONString(msgFileList), MsgFile.class);
        files.forEach(item -> {
            String name = item.getName();
            if (StringUtils.isNotEmpty(item.getExtension())) {
                name = name + "." + item.getExtension();
            }
            item.setName(name);
        });
        messageVo.setFiles(files);
    }



    public void parseUserIds(List<ForwardChannelInfo> channelInfos, LoginUser loginUser, List<Long> channelIds) {
        List<Long> userIds = new ArrayList<>();
        channelInfos.forEach(item -> {
            userIds.add(item.getDataId());
        });


        ChatChannel condition = new ChatChannel();
        condition.setType(CHANNEL_TYPE_DIRECT);
        condition.getParams().put("userId", loginUser.getUserid());
        condition.getParams().put("userIds", userIds);
        List<ChatChannel> channelList = chatChannelMapper.selectChannelByUserId(condition);

        List<Long> noChannelUsers = new ArrayList<>();


        if (CollectionUtils.isNotEmpty(channelList)) {
            LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper<>(ChannelMember.class);
            memberQueryWrapper.in(ChannelMember::getChannelId, channelList.stream().map(ChatChannel::getId).collect(Collectors.toList()));
            List<ChannelMember> channelMembers = channelMemberMapper.selectList(memberQueryWrapper);
            Map<Long, ChannelMember> channelMemberMap = channelMembers.stream().collect(Collectors.toMap(ChannelMember::getUserId, item -> item, (o, n) -> n));


            if (CollectionUtils.isNotEmpty(channelMembers)) {
                userIds.forEach(userId -> {
                    ChannelMember member = channelMemberMap.get(userId);
                    if (member != null) {
                        channelIds.add(member.getChannelId());
                    } else {
                        noChannelUsers.add(userId);
                    }
                });
            }
        } else {
            noChannelUsers.addAll(userIds);
        }


        if (CollectionUtils.isNotEmpty(noChannelUsers)) {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
                    SecurityContextHolder.setUserName(loginUser.getUsername());
                    SecurityContextHolder.setUserId(String.valueOf(loginUser.getUserid()));

                    try {
                        noChannelUsers.forEach(userId -> {
                            ChannelAddParam channel = new ChannelAddParam();
                            channel.setType(CHANNEL_TYPE_DIRECT);
                            List<Long> channelMembers = new ArrayList<>();
                            channelMembers.add(userId);
                            channelMembers.add(loginUser.getUserid());
                            channel.setChannelMembers(channelMembers);
                            chatChannelService.insertChatChannel(channel);
                            channelIds.add(channel.getId());
                        });
                    } catch (Exception e) {
                        status.setRollbackOnly();
                        throw e;
                    }
                }
            });
        }
    }

    private void parseForwardMessage(List<Long> forwardMsgIds, MessageVo messageVo) {
        MessageMetadata metaData = messageVo.getMetaData();
        MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metaData);

        statusFlag.setForwardFlag(Boolean.TRUE);
        Long messageId = messageVo.getId();
        LambdaQueryWrapper<MsgForward> msgForwardQueryWrapper = new LambdaQueryWrapper<>();
        msgForwardQueryWrapper.select(MsgForward::getForwardMsgId, MsgForward::getDeletedFlag);
        msgForwardQueryWrapper.eq(MsgForward::getMsgId, messageId);
        msgForwardQueryWrapper.eq(MsgForward::getDeletedFlag, DELETE_FLAG_TRUE);


        LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>(Message.class);
        messageQueryWrapper.in(Message::getId, forwardMsgIds);
        List<Message> forwardMessages = messageMapper.selectList(messageQueryWrapper);
        List<MessageVo> forwardMessageVos = processMetaData(forwardMessages, null, null);
        messageQueryService.processTextTagDatas(forwardMessageVos);
        processMessageFileInfo(forwardMessageVos);
        metaData.setMsgForwardData(forwardMessageVos);
    }



    private List<MessageVo> processMetaData(List<Message> messages, List<Long> messageIds, List<Long> ackMessageIds) {
        Map<Long, String> userIdNameMap = new HashMap<>();
        Map<Long, String> botIdMap = new HashMap<>();
        getUserIdNameMap(messages, userIdNameMap, botIdMap);

        List<Long> allMessageIds = new ArrayList<>(messages.size());
        Map<Long, MessageVo> messageIdMap = new HashMap<>(messages.size());

        List<MessageVo> result = new ArrayList<>(messages.size());
        messages.forEach(message -> {
            allMessageIds.add(message.getId());
            MessageVo vo = new MessageVo();
            BeanUtils.copyBeanProp(vo, message);
            if (MESSAGE_TYPE_USER.equals(vo.getType())) {
                vo.setCreateUser(userIdNameMap.get(vo.getUserId()));
            } else if (MESSAGE_TYPE_BOT.equals(vo.getType())) {
                vo.setCreateUser(botIdMap.get(vo.getUserId()));
            }
            vo.setProps(JSON.parseObject(com.soflyit.common.core.utils.StringUtils.defaultString(message.getPropsStr(), "{}"), MessageProp.class));

            MessageMetadata metadata = vo.getMetaData();
            if (metadata == null) {
                metadata = new MessageMetadata();
                vo.setMetaData(metadata);
                MetaCount metaCount = MessageMetaUtil.getCount(metadata);
                metaCount.setReplyCount(0);

            }

            result.add(vo);
            messageIdMap.put(message.getId(), vo);
        });





        processUnreadMsgForQuery(messageIds, ackMessageIds, result);


        processReplyMsgForQuery(allMessageIds, messageIdMap);


        processForwardMessage(messageIdMap, result);

        return result;
    }

    private void processForwardMessage(Map<Long, MessageVo> messageIdMap, List<MessageVo> result) {


        result.forEach(messageVo -> {
            if (messageVo.getProps() != null) {
                MessageProp prop = messageVo.getProps();
                Boolean forwardFlag = prop.getForwardFlag();
                if (forwardFlag != null && forwardFlag) {
                    parseForwardMessage(prop.getForwardMsgIds(), messageVo);
                }
            }
        });

    }


    private void processReplyMsgForQuery(List<Long> allMessageIds, Map<Long, MessageVo> messageMap) {

        LambdaQueryWrapper<MsgReply> replyQueryWrapper = new LambdaQueryWrapper<>();
        replyQueryWrapper.select(MsgReply::getId, MsgReply::getChannelId, MsgReply::getReplyCount);
        replyQueryWrapper.in(MsgReply::getId, allMessageIds);

        List<MsgReply> replyList = msgReplyMapper.selectList(replyQueryWrapper);
        if (CollectionUtils.isEmpty(replyList)) {
            return;
        }
        replyList.forEach(item -> {
            MessageVo messageVo = messageMap.get(item.getId());
            if (messageVo == null) {
                return;
            }
            MessageMetadata metadata = messageVo.getMetaData();
            if (metadata == null) {
                metadata = new MessageMetadata();
                messageVo.setMetaData(metadata);
            }
            MetaCount metaCount = MessageMetaUtil.getCount(metadata);
            metaCount.setReplyCount(item.getReplyCount());
        });
    }



    private void processUnreadMsgForQuery(List<Long> messageIds, List<Long> ackMessageIds, List<MessageVo> result) {
        Map<Long, MsgStatistic> stasticMap;
        Map<Long, List<MsgUnread>> unreadMap;
        Map<Long, List<MsgAck>> ackMap;
        if (CollectionUtils.isNotEmpty(messageIds)) {


            LambdaQueryWrapper<MsgStatistic> MsgStatisticQueryWrapper = new LambdaQueryWrapper<>();
            MsgStatisticQueryWrapper.select(MsgStatistic::getMsgId, MsgStatistic::getUserId, MsgStatistic::getUnreadCount, MsgStatistic::getUnAckCount);
            MsgStatisticQueryWrapper.in(MsgStatistic::getMsgId, messageIds);
            List<MsgStatistic> stasticList = msgStatisticMapper.selectList(MsgStatisticQueryWrapper);
            stasticMap = stasticList.stream().collect(Collectors.toMap(MsgStatistic::getMsgId, item -> item, (item1, item2) -> item1));


            LambdaQueryWrapper<MsgUnread> unreadQueryWrapper = new LambdaQueryWrapper<>();
            unreadQueryWrapper.select(MsgUnread::getMsgId, MsgUnread::getId, MsgUnread::getReadTime, MsgUnread::getUserId);
            unreadQueryWrapper.in(MsgUnread::getMsgId, messageIds);
            unreadQueryWrapper.isNull(MsgUnread::getReadTime);
            List<MsgUnread> unreadDetailList = msgUnreadMapper.selectList(unreadQueryWrapper);
            if (CollectionUtils.isNotEmpty(unreadDetailList)) {
                unreadMap = unreadDetailList.stream().collect(Collectors.groupingBy(MsgUnread::getMsgId));
            } else {
                unreadMap = null;
            }


            if (CollectionUtils.isNotEmpty(ackMessageIds)) {
                LambdaQueryWrapper<MsgAck> ackQueryWrapper = new LambdaQueryWrapper<>();
                ackQueryWrapper.select(MsgAck::getMsgId, MsgAck::getId, MsgAck::getAckTime, MsgAck::getUserId);
                ackQueryWrapper.in(MsgAck::getMsgId, ackMessageIds);
                ackQueryWrapper.isNull(MsgAck::getAckTime);
                List<MsgAck> ackList = msgAckMapper.selectList(ackQueryWrapper);
                if (CollectionUtils.isNotEmpty(ackList)) {
                    ackMap = ackList.stream().collect(Collectors.groupingBy(MsgAck::getMsgId));
                } else {
                    ackMap = null;
                }
            } else {
                ackMap = null;
            }
        } else {
            ackMap = null;
            unreadMap = null;
            stasticMap = null;
        }



        LoginUser user = SecurityUtils.getLoginUser();
        List<Long> messageIdExceptMe = result.stream().filter(item ->
                        (item.getProps().getFromBot() == null || !item.getProps().getFromBot()) && !item.getUserId().equals(user.getUserid()))
                .map(Message::getId).collect(Collectors.toList());
        Map<Long, MsgUnread> unreadMessageMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(messageIdExceptMe)) {
            LambdaQueryWrapper<MsgUnread> unreadCondition = new LambdaQueryWrapper<>();
            unreadCondition.select(MsgUnread::getId, MsgUnread::getMsgId);
            unreadCondition.eq(MsgUnread::getUserId, user.getUserid());
            unreadCondition.in(MsgUnread::getMsgId, messageIdExceptMe);
            unreadCondition.isNull(MsgUnread::getReadTime);

            List<MsgUnread> msgUnreadList = msgUnreadMapper.selectList(unreadCondition);
            if (CollectionUtils.isNotEmpty(msgUnreadList)) {
                for (MsgUnread unread : msgUnreadList) {
                    unreadMessageMap.put(unread.getMsgId(), unread);
                }
            }
        }

        result.forEach(item -> {
            MessageMetadata metadata = item.getMetaData();
            if (metadata == null) {
                metadata = new MessageMetadata();
                item.setMetaData(metadata);
            }

            MetaCount metaCount = MessageMetaUtil.getCount(metadata);
            if (MapUtils.isNotEmpty(stasticMap) && stasticMap.get(item.getId()) != null) {
                metaCount.setUnAckCount(stasticMap.get(item.getId()).getUnAckCount());
            }
            if (MapUtils.isNotEmpty(ackMap)) {
                metadata.setUnAckUserList(ackMap.get(item.getId()));
            }
        });
    }


    private void processMessageFileInfo(List<MessageVo> msgList) {
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
            if (com.soflyit.common.core.utils.StringUtils.isNotEmpty(item.getExtension())) {
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


    @Autowired
    public void setMsgForwardMapper(MsgForwardMapper msgForwardMapper) {
        this.msgForwardMapper = msgForwardMapper;
    }

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Autowired
    public void setChatChannelMapper(ChatChannelMapper chatChannelMapper) {
        this.chatChannelMapper = chatChannelMapper;
    }

    @Autowired
    public void setChannelMemberMapper(ChannelMemberMapper channelMemberMapper) {
        this.channelMemberMapper = channelMemberMapper;
    }

    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Autowired
    public void setUserNickNameService(UserNickNameService userNickNameService) {
        this.userNickNameService = userNickNameService;
    }

    @Autowired
    public void setChatChannelService(IChatChannelService chatChannelService) {
        this.chatChannelService = chatChannelService;
    }

    @Autowired
    public void setMsgUnreadMapper(MsgUnreadMapper msgUnreadMapper) {
        this.msgUnreadMapper = msgUnreadMapper;
    }

    @Autowired
    public void setMsgStatisticMapper(MsgStatisticMapper msgStatisticMapper) {
        this.msgStatisticMapper = msgStatisticMapper;
    }

    @Autowired
    public void setMsgAckMapper(MsgAckMapper msgAckMapper) {
        this.msgAckMapper = msgAckMapper;
    }

    @Autowired
    public void setChatEventService(ChatEventService chatEventService) {
        this.chatEventService = chatEventService;
    }

    @Autowired
    public void setMsgReplyMapper(MsgReplyMapper msgReplyMapper) {
        this.msgReplyMapper = msgReplyMapper;
    }

    @Autowired
    public void setMsgFileMapper(MsgFileMapper msgFileMapper) {
        this.msgFileMapper = msgFileMapper;
    }

    @Autowired
    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }
}
