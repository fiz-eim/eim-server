package com.soflyit.chattask.im.message.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.soflyit.chattask.im.bot.mapper.ImCardMapper;
import com.soflyit.chattask.im.bot.service.impl.CardDataHandlerManager;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ImCard;
import com.soflyit.chattask.im.channel.domain.param.CardData;
import com.soflyit.chattask.im.channel.domain.vo.MemberExtData;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.common.util.MessageMetaUtil;
import com.soflyit.chattask.im.message.domain.entity.*;
import com.soflyit.chattask.im.message.domain.param.MessageMentionQueryParam;
import com.soflyit.chattask.im.message.domain.param.MessageQueryParam;
import com.soflyit.chattask.im.message.domain.param.ThreadQueryParam;
import com.soflyit.chattask.im.message.domain.param.UserMessageQueryParam;
import com.soflyit.chattask.im.message.domain.vo.*;
import com.soflyit.chattask.im.message.mapper.*;
import com.soflyit.chattask.im.message.service.IMsgReactionService;
import com.soflyit.chattask.im.system.service.impl.UserNickNameService;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.PageUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.PageDomain;
import com.soflyit.common.core.web.page.TableSupport;
import com.soflyit.common.security.auth.AuthUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.soflyit.chattask.im.common.constant.MessageConstant.*;

/**
 * 消息查询服务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-24 09:28
 */
@Slf4j
@Component
public class MessageQueryService {

    private MessageMapper messageMapper;

    private MsgAckMapper msgAckMapper;

    private MsgUnreadMapper msgUnreadMapper;

    private MsgStatisticMapper msgStatisticMapper;

    private UserNickNameService userNickNameService;

    private ChannelMemberMapper channelMemberMapper;

    private MsgReplyMapper msgReplyMapper;

    @Autowired
    private MemberDeleteMsgMapper memberDeleteMsgMapper;

    @Autowired
    private ImCardMapper imCardMapper;

    @Autowired
    private CardDataHandlerManager cardDataHandlerManager;

    @Autowired
    private MsgFileMapper msgFileMapper;

    @Autowired
    private MsgTextTagMapper msgTextTagMapper;

    @Autowired
    private IMsgReactionService reactionService;



    public AjaxResult<List<MessageVo>> selectChannelMessageList(MessageQueryParam message) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        processDeleteMsg(message, loginUser);

        if (message.getFetchBefore() == null) {
            message.setFetchBefore(message.getMsgIdBefore() != null);
        }


        ChannelMember memberCondition = new ChannelMember();
        memberCondition.setChannelId(message.getChannelId());
        memberCondition.setUserId(loginUser.getUserid());

        List<ChannelMember> members = channelMemberMapper.selectChannelMemberList(memberCondition);
        List<Long> tagMsgIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(members)) {
            String extData = members.get(0).getExtData();
            MemberExtData memberExtData = JSON.parseObject(StringUtils.defaultString(extData, "{}"), MemberExtData.class);
            List<Long> msgIds = memberExtData.getTagMsgIds();
            if (msgIds != null) {
                tagMsgIds.addAll(msgIds);
            }
        }
        PageUtils.startPage();
        List<Message> messageList = messageMapper.selectMessageListByParam(message);

        return processQueryMessages(message, loginUser, messageList, tagMsgIds);
    }


    public AjaxResult<List<MessageVo>> selectMessageList(MessageQueryParam message) {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        Long channelId = message.getChannelId();
        List<Long> channelIds = new ArrayList<>();
        if (channelId == null) {

            Long userId = SecurityUtils.getUserId();

            List<ChannelMember> members = channelMemberMapper.selectChannelByUserId(userId);
            if (CollectionUtils.isNotEmpty(members)) {
                members.forEach(member -> {
                    channelIds.add(member.getChannelId());
                });
            }
            if (CollectionUtils.isNotEmpty(channelIds)) {
                message.setChannelIds(channelIds);
            }
        }
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageSize = pageDomain.getPageSize();
        if (pageSize != null && message.getMsgIdAround() != null) {
            message.setMsgIdBefore(message.getMsgIdAround());
            if (pageSize % 2 == 0) {
                pageDomain.setPageSize(pageSize / 2);
            } else {
                pageDomain.setPageSize(pageSize / 2 + 1);
            }
        }

        if (message.getFetchBefore() == null) {
            message.setFetchBefore(message.getMsgIdBefore() != null);
        }

        processDeleteMsg(message, loginUser);
        PageUtils.startPage(pageDomain);
        List<Message> messageList = messageMapper.selectMessageListByParam(message);
        if (message.getMsgIdAround() != null) {
            message.setMsgIdAfter(message.getMsgIdAround());
            MessageQueryParam nexParams = new MessageQueryParam();
            BeanUtils.copyBeanProp(nexParams, message);
            nexParams.setMsgIdAfter(nexParams.getMsgIdBefore());
            nexParams.setMsgIdBefore(null);
            nexParams.setFetchBefore(Boolean.FALSE);
            nexParams.setExcludeNextMsg(Boolean.TRUE);
            PageUtils.startPage(pageDomain);
            List<Message> preMessageList = messageMapper.selectMessageListByParam(nexParams);

            log.debug("消息信息：{}\n{}", JSON.toJSONString(messageList), JSON.toJSONString(preMessageList));

            messageList.addAll(preMessageList);
        }

        return processQueryMessages(message, loginUser, messageList, null);
    }

    private void processDeleteMsg(MessageQueryParam message, LoginUser loginUser) {
        Long channelId = message.getChannelId();
        List<Long> channelIds = new ArrayList<>();
        if (channelId != null) {
            channelIds.add(channelId);
        } else if (CollectionUtils.isNotEmpty(message.getChannelIds())) {
            channelIds.addAll(message.getChannelIds());
        }
        if (CollectionUtils.isEmpty(channelIds)) {
            return;
        }
        Long userId = loginUser.getUserid();
        LambdaQueryWrapper<MemberDeleteMsg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(MemberDeleteMsg::getId, MemberDeleteMsg::getMsgId);
        queryWrapper.eq(MemberDeleteMsg::getUserId, userId);
        queryWrapper.in(MemberDeleteMsg::getChannelId, channelIds);
        List<MemberDeleteMsg> memberDeleteMsgs = memberDeleteMsgMapper.selectList(queryWrapper);

        Set<Long> msgIds = memberDeleteMsgs.stream().map(MemberDeleteMsg::getMsgId).collect(Collectors.toSet());
        message.setExceptMsgIds(new ArrayList<Long>(msgIds));

    }

    @NotNull
    private AjaxResult<List<MessageVo>> processQueryMessages(MessageQueryParam message, LoginUser loginUser, List<Message> messageList, List<Long> tagMsgIds) {
        List<MessageVo> msgList;


        if (CollectionUtils.isNotEmpty(messageList)) {
            List<Long> messageIds = new ArrayList<>(messageList.size());
            List<Long> ackMessageIds = new ArrayList<>(messageList.size());
            List<Message> userMessages = messageList;


            if (loginUser != null) {
                userMessages = messageList.stream().filter(item -> item.getUserId().equals(loginUser.getUserid())).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(userMessages)) {
                userMessages.forEach(msg -> {
                    messageIds.add(msg.getId());
                    if (ACK_REQUIRED_TRUE.equals(msg.getRequestedAck())) {
                        ackMessageIds.add(msg.getId());
                    }
                });
            }

            msgList = processMetaDatas(messageList, messageIds, ackMessageIds, tagMsgIds, null);
            processMessageFileInfo(msgList);
        } else {
            msgList = new ArrayList<>();
        }
        msgList.sort(Comparator.comparing(MessageVo::getId));

        AjaxResult<List<MessageVo>> result = AjaxResult.success(msgList);

        Integer pageSize = PageUtils.pageSize();
        Integer pageNumber = PageUtils.pageNum();
        if (pageNumber == null) {
            pageNumber = 1;
        }
        PageInfo pageInfo = new PageInfo(messageList, pageSize);
        long total = pageInfo.getTotal();
        boolean hasNext = Long.valueOf(pageSize) * pageNumber < total;
        result.putExtData("hastNext", hasNext);

        if (hasNext) {
            Long nextMessageId = processNextMessage(messageList, message);
            result.putExtData("nextId", nextMessageId);
        }

        return result;
    }


    public AjaxResult<List<Long>> selectMentionMessageIdList(MessageMentionQueryParam param) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            String msg = "操作失败，没有登录或Token已过期";
            return AjaxResult.error(msg);
        }
        param.setUserId(loginUser.getUserid());
        MessageQueryParam queryParam = new MessageQueryParam();
        queryParam.setUserId(param.getUserId());
        queryParam.setChannelId(param.getChannelId());

        List<MsgUnread> messageList = msgUnreadMapper.selectMentionMessageIds(queryParam);
        List<Long> messageVoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(messageList)) {
            messageList.forEach(msgUnread -> {
                if (param.getContainReadMessage() != null && !param.getContainReadMessage()) {
                    if (msgUnread.getReadTime() == null) {
                        messageVoList.add(msgUnread.getMsgId());
                    }
                } else {
                    messageVoList.add(msgUnread.getMsgId());
                }
            });
        }

        return AjaxResult.success(messageVoList);
    }



    public AjaxResult<List<MessageVo>> selectUserChannelMessageList(UserMessageQueryParam param) {
        LoginUser user = AuthUtil.getLoginUser();

        ChannelMember memberCondition = new ChannelMember();
        memberCondition.setChannelId(param.getChannelId());
        memberCondition.setUserId(user.getUserid());

        List<ChannelMember> members = channelMemberMapper.selectChannelMemberList(memberCondition);
        ChannelMember member = null;
        Date lastViewTime = null;
        Long unreadCount = null;
        if (CollectionUtils.isNotEmpty(members)) {
            if (members.size() > 1) {
                String msg = "获取未读消息失败：成员信息异常";
                throw new BaseException(msg);
            }
            member = members.get(0);
            if (member == null) {
                String msg = "获取未读消息失败：没有权限";
                throw new BaseException(msg);
            }
            lastViewTime = member.getLastViewTime();
            unreadCount = member.getUnreadCount();
        }
        if (unreadCount == null) {
            unreadCount = 0L;
        }
        if (lastViewTime == null) {
            lastViewTime = new Date();
        }

        String extData = member.getExtData();
        MemberExtData memberExtData = JSON.parseObject(StringUtils.defaultString(extData, "{}"), MemberExtData.class);
        List<Long> tagMsgIds = memberExtData.getTagMsgIds();
        if (tagMsgIds == null) {
            tagMsgIds = new ArrayList<>();
            memberExtData.setTagMsgIds(tagMsgIds);
        }


        param.setCreateTime(lastViewTime);
        param.getParams().put("queryType", "before");
        List<Message> messageList = messageMapper.selectUserMessageListByParam(param);


        param.getParams().put("queryType", "after");
        List<Message> unreadMessageList = messageMapper.selectUserMessageListByParam(param);
        messageList.addAll(unreadMessageList);

        List<MessageVo> msgList;

        if (CollectionUtils.isNotEmpty(messageList)) {
            List<Long> ackMessageIds = new ArrayList<>(messageList.size());
            List<Long> userMessageIds = new ArrayList<>(messageList.size());
            messageList.forEach(msg -> {
                if (user != null && user.getUserid() != null && user.getUserid().equals(msg.getUserId())) {
                    userMessageIds.add(msg.getId());
                    if (ACK_REQUIRED_TRUE.equals(msg.getRequestedAck())) {
                        ackMessageIds.add(msg.getId());
                    }
                }
            });
            msgList = processMetaData(messageList, userMessageIds, ackMessageIds, tagMsgIds, null);


            processMessageFileInfo(msgList);
        } else {
            msgList = new ArrayList<>();
        }


        msgList.sort(Comparator.comparing(MessageVo::getId));

        AjaxResult<List<MessageVo>> result = AjaxResult.success(msgList);

        int total = unreadMessageList.size();
        result.putExtData("hastNext", Long.valueOf(total) < unreadCount);
        return result;
    }



    protected List<MessageVo> processMetaData(List<Message> messages, List<Long> messageIds, List<Long> ackMessageIds, List<Long> tagMsgIds, String token) {
        LoginUser currentUser = SecurityUtils.getLoginUser();
        Map<Long, String> userIdNameMap = new HashMap<>();
        Map<Long, String> botIdMap = new HashMap<>();
        getUserIdNameMap(messages, userIdNameMap, botIdMap);

        List<Long> allMessageIds = messages.stream().map(Message::getId).collect(Collectors.toList());
        Map<Long, MessageVo> messageIdMap = new HashMap<>(messages.size());

        if (tagMsgIds == null) {
            tagMsgIds = new ArrayList<>();
        }


        List<MessageVo> result = new ArrayList<>(messages.size());
        List<Long> finalTagMsgIds = tagMsgIds;
        messages.forEach(message -> {
            MessageVo vo = new MessageVo();
            BeanUtils.copyBeanProp(vo, message);
            if (MESSAGE_TYPE_USER.equals(vo.getType())) {
                vo.setCreateUser(userIdNameMap.get(vo.getUserId()));
            } else if (MESSAGE_TYPE_BOT.equals(vo.getType())) {
                vo.setCreateUser(botIdMap.get(vo.getUserId()));
            }

            MessageMetadata metadata = vo.getMetaData();
            if (metadata == null) {
                metadata = new MessageMetadata();
                vo.setMetaData(metadata);

                MetaCount metaCount = MessageMetaUtil.getCount(metadata);
                metaCount.setReplyCount(0);

            }

            MessageProp props = JSON.parseObject(StringUtils.defaultString(message.getPropsStr(), "{}"), MessageProp.class);
            processCardData(props, metadata, currentUser);
            vo.setProps(props);

            String mentionUsers = vo.getMentionUsers();

            MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);
            if (currentUser != null) {
                List<Long> mentUserIdList = JSON.parseArray(StringUtils.defaultString(mentionUsers, "[]"), Long.class);
                statusFlag.setMentionMe(mentUserIdList.contains(currentUser.getUserid()));
            } else {
                statusFlag.setMentionMe(Boolean.FALSE);
            }

            processRefReplyMessage(vo);
            processTagFlag(vo, finalTagMsgIds);
            processPinnedData(metadata, props);
            processTextTagData(metadata, message);

            result.add(vo);
            messageIdMap.put(message.getId(), vo);
        });

        reactionService.processReactionData(allMessageIds, result);


        processUnreadMsgForQuery(messageIds, ackMessageIds, result, token);


        processReplyMsgForQuery(allMessageIds, messageIdMap);


        processForwardMessage(messageIdMap, result, token);

        return result;
    }


    protected List<MessageVo> processMetaDatas(List<Message> messages, List<Long> messageIds, List<Long> ackMessageIds, List<Long> tagMsgIds, String token) {
        LoginUser currentUser = SecurityUtils.getLoginUser();
        Map<Long, String> userIdNameMap = new HashMap<>();
        Map<Long, String> botIdMap = new HashMap<>();
        getUserIdNameMap(messages, userIdNameMap, botIdMap);

        List<Long> allMessageIds = messages.stream().map(Message::getId).collect(Collectors.toList());
        Map<Long, MessageVo> messageIdMap = new HashMap<>(messages.size());

        if (tagMsgIds == null) {
            tagMsgIds = new ArrayList<>();
        }

        List<MessageVo> result = new ArrayList<>(messages.size());
        List<Long> finalTagMsgIds = tagMsgIds;
        messages.forEach(message -> {
            MessageVo vo = new MessageVo();
            BeanUtils.copyBeanProp(vo, message);
            if (MESSAGE_TYPE_USER.equals(vo.getType())) {
                vo.setCreateUser(userIdNameMap.get(vo.getUserId()));
            } else if (MESSAGE_TYPE_BOT.equals(vo.getType())) {
                vo.setCreateUser(botIdMap.get(vo.getUserId()));
            }

            MessageMetadata metadata = vo.getMetaData();
            if (metadata == null) {
                metadata = new MessageMetadata();
                vo.setMetaData(metadata);

                MetaCount metaCount = MessageMetaUtil.getCount(metadata);
                metaCount.setReplyCount(0);
            }

            MessageProp props = JSON.parseObject(StringUtils.defaultString(message.getPropsStr(), "{}"), MessageProp.class);
            processCardData(props, metadata, currentUser);
            vo.setProps(props);

            if (props.getSystemData() != null) {
                metadata.setSystemData(props.getSystemData());
            }
            String mentionUsers = vo.getMentionUsers();
            MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);
            if (currentUser != null) {
                List<Long> mentUserIdList = JSON.parseArray(StringUtils.defaultString(mentionUsers, "[]"), Long.class);
                statusFlag.setMentionMe(mentUserIdList.contains(currentUser.getUserid()));
            } else {
                statusFlag.setMentionMe(Boolean.FALSE);
            }

            processTagFlag(vo, finalTagMsgIds);
            processPinnedData(metadata, props);

            result.add(vo);
            messageIdMap.put(message.getId(), vo);
        });


        processRefReplyMessages(result);

        processTextTagDatas(result);

        reactionService.processReactionData(allMessageIds, result);


        processUnreadMsgForQuery(messageIds, ackMessageIds, result, token);


        processReplyMsgForQuery(allMessageIds, messageIdMap);


        processForwardMessage(messageIdMap, result, token);

        return result;
    }



    private void processTextTagData(MessageMetadata metadata, Message message) {
        MsgTextTag textTag = msgTextTagMapper.selectById(message.getId());
        if (textTag == null) {
            metadata.setTextTags(new ArrayList<>());
        } else {
            String tagData = textTag.getTagData();
            TextTagData textTagData = JSON.parseObject(StringUtils.defaultString(tagData, "{}"), TextTagData.class);
            if (textTagData.getTags() == null) {
                metadata.setTextTags(new ArrayList<>());
            } else {
                metadata.setTextTags(textTagData.getTags());
            }
        }
    }


    protected void processTextTagDatas(List<MessageVo> vos) {
        List<Long> msgIds = vos.stream().map(item -> item.getId()).collect(Collectors.toList());

        LambdaQueryWrapper<MsgTextTag> msgTextTagQueryWrapper = new LambdaQueryWrapper<>(MsgTextTag.class);
        msgTextTagQueryWrapper.in(MsgTextTag::getMsgId, msgIds);
        List<MsgTextTag> msgTextTags = msgTextTagMapper.selectList(msgTextTagQueryWrapper);
        Map<Long, MsgTextTag> msgTextTagMap = msgTextTags.stream().collect(Collectors.toMap(item -> item.getMsgId(), item -> item, (v1, v2) -> v1));

        vos.forEach(item -> {
            MsgTextTag textTag = msgTextTagMap.get(item.getId());
            MessageMetadata metadata = item.getMetaData();
            if (textTag == null) {
                metadata.setTextTags(new ArrayList<>());
            } else {
                String tagData = textTag.getTagData();
                TextTagData textTagData = JSON.parseObject(StringUtils.defaultString(tagData, "{}"), TextTagData.class);
                if (textTagData.getTags() == null) {
                    metadata.setTextTags(new ArrayList<>());
                } else {
                    metadata.setTextTags(textTagData.getTags());
                }
            }
        });
    }
    protected void processPinnedData(MessageMetadata metadata, MessageProp props) {
        MetaStatusFlag metaStatusFlag = MessageMetaUtil.getStatusFlag(metadata);
        metaStatusFlag.setPinnedFlag(Boolean.FALSE);
        if (props.getPinnedFlag() != null && props.getPinnedFlag()) {
            metaStatusFlag.setPinnedFlag(props.getPinnedFlag());
            metadata.setPinnedUserId(props.getPinnedUserId());
            metadata.setPinnedUser(props.getPinnedUser());
        }
    }

    private void processPinnedDatas(List<MessageVo> messageVos) {
        messageVos.forEach(item -> {
            MessageMetadata metadata = item.getMetaData();
            MessageProp props = item.getProps();
            MetaStatusFlag metaStatusFlag = MessageMetaUtil.getStatusFlag(metadata);
            metaStatusFlag.setPinnedFlag(Boolean.FALSE);
            if (props.getPinnedFlag() != null && props.getPinnedFlag()) {
                metaStatusFlag.setPinnedFlag(props.getPinnedFlag());
                metadata.setPinnedUserId(props.getPinnedUserId());
                metadata.setPinnedUser(props.getPinnedUser());
            }
        });

    }


    protected void processCardData(MessageProp props, MessageMetadata metadata, LoginUser currentUser) {
        if ("card".equals(props.getContentType())) {
            Map<String, Object> messageParams = props.getMessageParams();
            if (messageParams == null) {
                return;
            }
            Object cardId = messageParams.get("cardId");
            if (cardId instanceof Long) {
                ImCard card = imCardMapper.selectById((Long) cardId);
                if (card == null) {
                    return;
                }

                CardData cardData = (JSON.parseObject(card.getCardData(), CardData.class));
                if (currentUser != null) {
                    cardDataHandlerManager.getHandler(card.getTemplateId()).processData(cardData, currentUser);
                }

                MetaCardInfo metaCard = MessageMetaUtil.getMetaCard(metadata);
                metaCard.setCardData(cardData);
                metaCard.setCardInfo(props.getMessageParams());
                props.setMessageParams(null);
            }
        }
    }

    private void processTagFlag(MessageVo vo, List<Long> finalTagMsgIds) {
        MessageMetadata metadata = vo.getMetaData();
        MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);
        statusFlag.setTagFlag(finalTagMsgIds.contains(vo.getId()));
    }


    private void processTagFlags(List<MessageVo> vos, List<Long> finalTagMsgIds) {
        vos.forEach(vo -> {
            MessageMetadata metadata = vo.getMetaData();
            MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);
            statusFlag.setTagFlag(finalTagMsgIds.contains(vo.getId()));
        });
    }

    private void processRefReplyMessage(MessageVo vo) {
        MessageProp prop = vo.getProps();
        MessageMetadata metadata = vo.getMetaData();
        MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);
        if (prop.getReplyFlag() != null) {
            statusFlag.setReplyFlag(prop.getReplyFlag());
        } else {
            statusFlag.setReplyFlag(Boolean.FALSE);
            return;
        }
        if (prop.getReplyMsgId() != null) {
            metadata.setReplyMsgId(prop.getReplyMsgId());
        } else {
            statusFlag.setReplyFlag(Boolean.FALSE);
            return;
        }

        Message refMessage = messageMapper.selectMessageById(prop.getReplyMsgId());
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

    protected void processRefReplyMessages(List<MessageVo> vos) {
        List<Long> replyMsgIds = new ArrayList<>();
        Map<Long, Long> msgIdReplyMap = new HashMap<>();
        List<MessageVo> replyMsgs = vos.stream().filter(item -> {
            MessageProp prop = item.getProps();
            MessageMetadata metadata = item.getMetaData();
            MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);
            if (prop.getReplyFlag() != null) {
                statusFlag.setReplyFlag(prop.getReplyFlag());
            } else {
                statusFlag.setReplyFlag(Boolean.FALSE);
            }
            if (statusFlag.getReplyFlag() && prop.getReplyMsgId() != null) {
                metadata.setReplyMsgId(prop.getReplyMsgId());
            } else {
                statusFlag.setReplyFlag(Boolean.FALSE);
            }
            if (statusFlag.getReplyFlag()) {
                replyMsgIds.add(prop.getReplyMsgId());
                msgIdReplyMap.put(item.getId(), prop.getReplyMsgId());
            }
            return statusFlag.getReplyFlag();
        }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(replyMsgIds)) {
            return;
        }

        LambdaQueryWrapper<Message> refMsgQueryWrapper = new LambdaQueryWrapper<>(Message.class);
        refMsgQueryWrapper.in(Message::getId, replyMsgIds);
        List<Message> refMessages = messageMapper.selectList(refMsgQueryWrapper);
        if (CollectionUtils.isNotEmpty(refMessages)) {
            Map<Long, MessageVo> replyMessageVoMap = new HashMap<>();
            List<MessageVo> messageReplyVos = refMessages.stream().map(item -> {
                MessageVo messageVo = new MessageVo();
                BeanUtils.copyBeanProp(messageVo, item);
                MessageMetadata replyMetadata = messageVo.getMetaData();
                if (replyMetadata == null) {
                    replyMetadata = new MessageMetadata();
                    messageVo.setMetaData(replyMetadata);
                }
                replyMessageVoMap.put(messageVo.getId(), messageVo);
                return messageVo;
            }).collect(Collectors.toList());

            processReplyMsgMetadatas(messageReplyVos);

            replyMsgs.forEach(item -> {
                Long messageId = item.getId();
                Long replyMsgId = msgIdReplyMap.get(messageId);
                MessageMetadata metadata = item.getMetaData();
                MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);
                if (replyMsgId == null) {
                    statusFlag.setReplyFlag(Boolean.FALSE);
                } else {
                    MessageVo replyMsg = replyMessageVoMap.get(replyMsgId);
                    metadata.setReplyMsgData(replyMsg);
                }
            });
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

        if (messageProp.getForwardFlag() != null) {
            MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);
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


    private void processReplyMsgMetadatas(List<MessageVo> messageVos) {

        Map<Long, MessageVo> messageVoMap = new HashMap<>();
        List<Long> messageIds = new ArrayList<>();
        messageVos.forEach(messageVo -> {
            MessageMetadata metadata = messageVo.getMetaData();
            if (metadata == null) {
                metadata = new MessageMetadata();
                messageVo.setMetaData(metadata);
            }
            MessageProp messageProp = JSON.parseObject(StringUtils.defaultString(messageVo.getPropsStr(), "{}"), MessageProp.class);
            if (messageProp.getForwardFlag() != null) {
                MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);
                statusFlag.setForwardFlag(messageProp.getForwardFlag());
            }
            messageVoMap.put(messageVo.getId(), messageVo);
            messageIds.add(messageVo.getId());
        });
        reactionService.processReactionData(messageIds, messageVos);

        LambdaQueryWrapper<MsgFile> msgFileQueryWrapper = new LambdaQueryWrapper<>();
        msgFileQueryWrapper.select(MsgFile::getMsgId, MsgFile::getName, MsgFile::getId, MsgFile::getSize, MsgFile::getExternalId,
                MsgFile::getExtension, MsgFile::getHasPreviewImage, MsgFile::getWidth, MsgFile::getHeight, MsgFile::getMiniPreview);
        msgFileQueryWrapper.in(MsgFile::getMsgId, messageIds);
        msgFileQueryWrapper.eq(MsgFile::getDeleteTime, -1L);
        List<MsgFile> msgFileList = msgFileMapper.selectList(msgFileQueryWrapper);
        Map<Long, List<MsgFile>> msgFileMap = msgFileList.stream().collect(Collectors.groupingBy(MsgFile::getMsgId));
        messageVos.forEach(messageVo -> {
            List<MsgFile> msgFiles = msgFileMap.get(messageVo.getId());
            if (msgFiles != null) {
                messageVo.setFiles(msgFiles);
            }
        });
    }



    protected List<MessageVo> processMetaDataForForward(List<Message> messages, List<Long> messageIds, List<Long> ackMessageIds, String token) {
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
            vo.setProps(JSON.parseObject(StringUtils.defaultString(message.getPropsStr(), "{}"), MessageProp.class));

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

        processForwardMessage(messageIdMap, result, token);

        return result;
    }

    protected void processForwardMessage(Map<Long, MessageVo> messageIdMap, List<MessageVo> result, String token) {

        Map<Long, List<Long>> msgIdForwardMap = new HashMap<>();

        result.forEach(messageVo -> {
            if (messageVo.getProps() != null) {
                MessageProp prop = messageVo.getProps();
                Boolean forwardFlag = prop.getForwardFlag();
                if (forwardFlag != null && forwardFlag) {
                    msgIdForwardMap.put(messageVo.getId(), prop.getForwardMsgIds());

                }
            }
        });
        parseForwardMessages(result, msgIdForwardMap, token);

    }

    public void parseForwardMessage(List<Long> forwardMsgIds, MessageVo messageVo, String token) {
        if (CollUtil.isEmpty(forwardMsgIds)) {
            return;
        }

        MessageMetadata metaData = messageVo.getMetaData();
        MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metaData);
        statusFlag.setForwardFlag(Boolean.TRUE);

        LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>(Message.class);
        messageQueryWrapper.in(Message::getId, forwardMsgIds);

        List<Message> forwardMessages = messageMapper.selectList(messageQueryWrapper);
        List<MessageVo> forwardMessageVos = processMetaData(forwardMessages, null, null, null, token);

        forwardMessageVos.forEach(messageItem -> {
            messageItem.getMetaData().setReactions(new ArrayList<>());
        });
        processMessageFileInfo(forwardMessageVos);
        metaData.setMsgForwardData(forwardMessageVos);
    }

    public void parseForwardMessages(List<MessageVo> messageVos, Map<Long, List<Long>> msgIdForwardMap, String token) {
        if (MapUtils.isEmpty(msgIdForwardMap)) {
            return;
        }
        Set<Long> forwardMsgIds = new HashSet<>();
        msgIdForwardMap.forEach((key, val) -> {
            forwardMsgIds.addAll(val);
        });
        if (CollectionUtils.isEmpty(forwardMsgIds)) {
            return;
        }
        LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>(Message.class);
        messageQueryWrapper.in(Message::getId, forwardMsgIds);

        List<Message> forwardMessages = messageMapper.selectList(messageQueryWrapper);
        List<MessageVo> forwardMessageVos = processMetaDatas(forwardMessages, null, null, null, token);
        Map<Long, MessageVo> forwardMsgMap = new HashMap<>();
        forwardMessageVos.forEach(messageItem -> {
            messageItem.getMetaData().setReactions(new ArrayList<>());
            forwardMsgMap.put(messageItem.getId(), messageItem);
        });

        processMessageFileInfo(forwardMessageVos);
        messageVos.forEach(messageVo -> {
            Long msgId = messageVo.getId();
            List<Long> forwardMsgIdList = msgIdForwardMap.get(msgId);
            if (CollectionUtils.isNotEmpty(forwardMsgIdList)) {
                List<MessageVo> forwardMessageList = new ArrayList<>();
                forwardMsgIdList.forEach(forwardMsgId -> {
                    forwardMessageList.add(forwardMsgMap.get(forwardMsgId));
                });
                MessageMetadata metaData = messageVo.getMetaData();
                MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metaData);
                statusFlag.setForwardFlag(Boolean.TRUE);
                metaData.setMsgForwardData(forwardMessageList);
            }
        });
    }



    public AjaxResult<List<MessageVo>> selectThreadMessages(Long msgId, ThreadQueryParam threadQueryParam) {

        Message rootMessage = messageMapper.selectMessageById(msgId);
        if (rootMessage == null) {
            String msg = "获取回复失败，消息不存在";
            return AjaxResult.error(msg);
        }


        Integer pageNumber = PageUtils.pageNum();
        if (pageNumber == null) {
            pageNumber = 1;
        }
        Integer pageSize = PageUtils.pageSize();
        if (pageSize == null) {
            pageSize = 30;
        }

        PageUtils.startPage(pageNumber, pageSize);
        LoginUser loginUser = SecurityUtils.getLoginUser();


        List<MessageVo> messageVos;
        PageInfo pageInfo;

        if (threadQueryParam.getSkipFetchThreads() != null && threadQueryParam.getSkipFetchThreads()) {
            LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>();
            messageQueryWrapper.select(Message::getId);
            messageQueryWrapper.eq(Message::getRootId, msgId);
            if (threadQueryParam.getFromMessageId() != null) {
                messageQueryWrapper.gt(Message::getId, threadQueryParam.getFromMessageId());
            }
            if (threadQueryParam.getFromCreateTime() != null) {
                messageQueryWrapper.gt(Message::getCreateTime, threadQueryParam.getFromCreateTime());
            }
            if (StringUtils.equals(THREAD_QUERY_DIRECTION_UP, threadQueryParam.getDirection())) {
                messageQueryWrapper.orderByAsc(Message::getId);
            }
            if (StringUtils.equals(THREAD_QUERY_DIRECTION_DOWN, threadQueryParam.getDirection())) {
                messageQueryWrapper.orderByDesc(Message::getId);
            }

            List<Message> messages = messageMapper.selectList(messageQueryWrapper);
            pageInfo = new PageInfo(messages, pageSize);
            if (CollectionUtils.isNotEmpty(messages)) {
                messageVos = new ArrayList<>(messages.size());
            } else {
                messageVos = null;
            }
            messages.forEach(msg -> {
                MessageVo msgVo = new MessageVo();
                msgVo.setId(msg.getId());
                messageVos.add(msgVo);
            });
        } else {
            LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>(Message.class);
            messageQueryWrapper.eq(Message::getRootId, msgId);
            if (threadQueryParam.getFromMessageId() != null) {
                messageQueryWrapper.gt(Message::getId, threadQueryParam.getFromMessageId());
            }
            if (threadQueryParam.getFromCreateTime() != null) {
                messageQueryWrapper.gt(Message::getCreateTime, threadQueryParam.getFromCreateTime());
            }
            if (StringUtils.equals(THREAD_QUERY_DIRECTION_UP, threadQueryParam.getDirection())) {
                messageQueryWrapper.orderByAsc(Message::getId);
            }
            if (StringUtils.equals(THREAD_QUERY_DIRECTION_DOWN, threadQueryParam.getDirection())) {
                messageQueryWrapper.orderByDesc(Message::getId);
            }

            List<Message> messageList = messageMapper.selectList(messageQueryWrapper);

            pageInfo = new PageInfo(messageList, pageSize);

            if (pageNumber == 1) {
                messageList.add(0, rootMessage);
            }


            if (CollectionUtils.isNotEmpty(messageList)) {
                List<Long> messageIds = new ArrayList<>(messageList.size());
                List<Long> ackMessageIds = new ArrayList<>(messageList.size());
                List<Message> userMessages = messageList;


                if (loginUser != null) {
                    userMessages = messageList.stream().filter(item -> item.getUserId().equals(loginUser.getUserid())).collect(Collectors.toList());
                }
                if (CollectionUtils.isNotEmpty(userMessages)) {
                    userMessages.forEach(msg -> {
                        messageIds.add(msg.getId());
                        if (ACK_REQUIRED_TRUE.equals(msg.getRequestedAck())) {
                            ackMessageIds.add(msg.getId());
                        }
                    });
                }


                ChannelMember memberCondition = new ChannelMember();
                memberCondition.setChannelId(rootMessage.getChannelId());
                memberCondition.setUserId(loginUser.getUserid());

                List<ChannelMember> members = channelMemberMapper.selectChannelMemberList(memberCondition);
                List<Long> tagMsgIds = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(members)) {
                    String extData = members.get(0).getExtData();
                    MemberExtData memberExtData = JSON.parseObject(StringUtils.defaultString(extData, "{}"), MemberExtData.class);
                    List<Long> msgIds = memberExtData.getTagMsgIds();
                    if (msgIds != null) {
                        tagMsgIds.addAll(msgIds);
                    }
                }

                messageVos = processMetaData(messageList, messageIds, ackMessageIds, tagMsgIds, null);
                processMessageFileInfo(messageVos);
            } else {
                messageVos = new ArrayList<>();
            }
        }

        messageVos.forEach(item -> {
            item.setPropsStr(null);
            item.setProps(null);
        });

        AjaxResult<List<MessageVo>> result = AjaxResult.success(messageVos);
        long total = pageInfo.getTotal();
        result.putExtData("hastNext", Long.valueOf(pageSize) * pageNumber < total);
        return AjaxResult.success(messageVos);
    }


    protected void processReplyMsgForQuery(List<Long> allMessageIds, Map<Long, MessageVo> messageMap) {

        LambdaQueryWrapper<MsgReply> replyQueryWrapper = new LambdaQueryWrapper<>();
        replyQueryWrapper.select(MsgReply::getId, MsgReply::getChannelId, MsgReply::getReplyCount, MsgReply::getExtData);
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
            MetaCount count = MessageMetaUtil.getCount(metadata);
            count.setReplyCount(item.getReplyCount());

            MsgReplyExt ext = JSON.parseObject(StringUtils.defaultString(item.getExtData(), "{\"availableUsers\":[]}"), MsgReplyExt.class);
            metadata.setAvailableReplyUsers(ext.getAvailableUsers());


        });
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


    protected void processMessageFileInfo(MessageReadVo messageReadVo) {
        MessageVo messageVo = new MessageVo();
        BeanUtils.copyBeanProp(messageVo, messageReadVo);
        List<MessageVo> messageVos = new ArrayList<>();
        messageVos.add(messageVo);
        processMessageFileInfo(messageVos);
        messageReadVo.setFiles(messageVo.getFiles());
    }



    protected void processUnreadMsgForQuery(List<Long> messageIds, List<Long> ackMessageIds, List<MessageVo> result, String token) {
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
        if (user == null && StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(token)) {
            user = AuthUtil.getLoginUser(token);
        }

        LoginUser finalUser = user;
        List<Long> messageIdExceptMe = result.stream().filter(item -> (finalUser != null && !item.getUserId().equals(finalUser.getUserid())))
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

            MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);

            statusFlag.setReadFlag(Boolean.TRUE);
            if (finalUser != null && !item.getUserId().equals(finalUser.getUserid()) && unreadMessageMap.get(item.getId()) != null) {
                statusFlag.setReadFlag(Boolean.FALSE);
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


    protected void processUnreadMsgForHistory(List<Long> messageIds, List<MessageVo> result) {
        Map<Long, MsgStatistic> stasticMap;
        Map<Long, List<MsgUnread>> unreadMap;
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

        } else {
            unreadMap = null;
            stasticMap = null;
        }
        result.forEach(item -> {
            MessageMetadata metadata = item.getMetaData();
            if (metadata == null) {
                metadata = new MessageMetadata();
                item.setMetaData(metadata);
            }

            MetaStatusFlag statusFlag = MessageMetaUtil.getStatusFlag(metadata);

            statusFlag.setReadFlag(Boolean.TRUE);

            MetaCount metaCount = MessageMetaUtil.getCount(metadata);

            if (MapUtils.isNotEmpty(stasticMap) && stasticMap.get(item.getId()) != null) {
                metaCount.setUnAckCount(stasticMap.get(item.getId()).getUnAckCount());
            }
        });
    }


    private Long processNextMessage(List<Message> messageList, MessageQueryParam message) {

        Long nextMessageId = null;
        Long messageId = messageList.get(messageList.size() - 1).getId();
        if (message.getMsgIdAfter() != null) {
            LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(Message::getId);
            if (message.getChannelId() != null) {
                queryWrapper.eq(Message::getChannelId, message.getChannelId());
            } else if (CollectionUtils.isNotEmpty(message.getChannelIds())) {
                queryWrapper.in(Message::getChannelId, message.getChannelIds());
            } else {
                return null;
            }
            if (StringUtils.isNotEmpty(message.getContent())) {
                queryWrapper.like(Message::getContent, message.getContent());
            }
            queryWrapper.gt(Message::getId, messageId);
            queryWrapper.orderByAsc(Message::getId);
            queryWrapper.last(" limit 1");
            List<Message> messages = messageMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(messages)) {
                nextMessageId = messages.get(0).getId();
            } else {
                nextMessageId = messageId;
            }
        } else {
            LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(Message::getId);
            if (message.getChannelId() != null) {
                queryWrapper.eq(Message::getChannelId, message.getChannelId());
            } else if (CollectionUtils.isNotEmpty(message.getChannelIds())) {
                queryWrapper.in(Message::getChannelId, message.getChannelIds());
            } else {
                return null;
            }
            if (StringUtils.isNotEmpty(message.getContent())) {
                queryWrapper.like(Message::getContent, message.getContent());
            }
            queryWrapper.lt(Message::getId, messageId);
            queryWrapper.orderByDesc(Message::getId);
            queryWrapper.last(" limit 1");
            List<Message> messages = messageMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(messages)) {
                nextMessageId = messages.get(0).getId();
            } else {
                nextMessageId = messageId;
            }
        }

        return nextMessageId;
    }


    public Map<Long, String> getUserIdNameMap(List<Message> messages, Map<Long, String> userIdMap, Map<Long, String> botIdMap) {

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
    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Autowired
    public void setMsgAckMapper(MsgAckMapper msgAckMapper) {
        this.msgAckMapper = msgAckMapper;
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
    public void setChannelMemberMapper(ChannelMemberMapper channelMemberMapper) {
        this.channelMemberMapper = channelMemberMapper;
    }

    @Autowired
    public void setMsgReplyMapper(MsgReplyMapper msgReplyMapper) {
        this.msgReplyMapper = msgReplyMapper;
    }

    @Autowired
    public void setUserNickNameService(UserNickNameService userNickNameService) {
        this.userNickNameService = userNickNameService;
    }
}
