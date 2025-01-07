package com.soflyit.chattask.im.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgReply;
import com.soflyit.chattask.im.message.domain.entity.MsgUnread;
import com.soflyit.chattask.im.message.domain.param.MessageAddParam;
import com.soflyit.chattask.im.message.domain.param.MsgReplyVo;
import com.soflyit.chattask.im.message.domain.vo.MentionData;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.im.message.domain.vo.MsgReplyExt;
import com.soflyit.chattask.im.message.mapper.MessageMapper;
import com.soflyit.chattask.im.message.mapper.MsgReplyMapper;
import com.soflyit.chattask.im.message.mapper.MsgUnreadMapper;
import com.soflyit.chattask.im.message.service.IMsgReplyService;
import com.soflyit.chattask.im.system.service.impl.UserNickNameService;
import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.model.LoginUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 消息回复统计Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Service
public class MsgReplyServiceImpl implements IMsgReplyService {

    private MsgReplyMapper msgReplyMapper;

    private UserNickNameService userNickNameService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MsgUnreadMapper msgUnreadMapper;

    @Autowired
    private MessageQueryService messageQueryService;

    @Autowired
    private MessagePushService messagePushService;



    @Override
    public MsgReply selectMsgReplyById(Long id) {
        return msgReplyMapper.selectMsgReplyById(id);
    }


    @Override
    public List<MsgReply> selectMsgReplyList(MsgReply msgReply) {
        return msgReplyMapper.selectMsgReplyList(msgReply);
    }


    @Override
    public int insertMsgReply(MsgReply msgReply) {
        msgReply.setCreateTime(DateUtils.getNowDate());
        return msgReplyMapper.insertMsgReply(msgReply);
    }


    @Override
    public int updateMsgReply(MsgReply msgReply) {
        msgReply.setUpdateTime(DateUtils.getNowDate());
        return msgReplyMapper.updateMsgReply(msgReply);
    }


    @Override
    public int deleteMsgReplyByIds(Long[] ids) {
        return msgReplyMapper.deleteMsgReplyByIds(ids);
    }


    @Override
    public int deleteMsgReplyById(Long id) {
        return msgReplyMapper.deleteMsgReplyById(id);
    }


    @Override
    public void processReplyMessage(MessageAddParam message) {
        Long msgId = message.getRootId();
        MsgReply msgReply = null;
        if (msgId != null) {
            msgReply = selectMsgReplyById(msgId);
        }

        Integer replyCount = 0;
        if (msgReply == null) {
            msgReply = new MsgReply();
            msgReply.setId(msgId);
            msgReply.setReplyCount(replyCount);
            msgReply.setChannelId(message.getChannelId());

            MsgReplyExt replyExt = new MsgReplyExt();
            List<Long> availableUsers = new ArrayList<>();
            replyExt.setAvailableUsers(availableUsers);
            msgReply.setExtData(JSON.toJSONString(replyExt));

            insertMsgReply(msgReply);
        } else {
            replyCount = msgReply.getReplyCount();
        }
        replyCount++;
        msgReply.setReplyCount(replyCount);
        msgReply.setLastReplyTime(message.getCreateTime());
        List<SysUser> users = JSON.parseArray(StringUtils.defaultString(msgReply.getParticipants(), "[]"), SysUser.class);
        SysUser user = null;
        String nickName = userNickNameService.getNickName(message.getUserId());
        List<SysUser> existUser = users.stream().filter(item -> item.getUserId() != null && item.getUserId().equals(message.getUserId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(existUser)) {
            user = new SysUser();
            user.setUserId(message.getUserId());
            user.setNickName(nickName);
            users.add(user);
            msgReply.setParticipants(JSON.toJSONString(users));
        }

        msgReplyMapper.updateMsgReply(msgReply);
    }



    @Override
    public AjaxResult getReplyInfo(Long messageId) {
        MsgReply msgReply = selectMsgReplyById(messageId);
        if (msgReply == null) {
            msgReply = new MsgReply();
            msgReply.setId(messageId);
            msgReply.setReplyCount(0);
        }
        return AjaxResult.success(msgReply);
    }


    @Override
    public AjaxResult topicMsgConfig(Long messageId, MsgReplyExt replyExt) {
        return AjaxResult.error("暂不支持");
    }


    @Override
    public AjaxResult selectTopicList(MsgReply reply) {
        if (reply == null) {
            return AjaxResult.error("查询话题列表失败，参数不能为空");
        }
        if (reply.getChannelId() == null) {
            return AjaxResult.error("查询话题列表失败，群组Id不能为空");
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("查询话题列表失败，获取用户信息失败");
        }

        MsgReply condition = new MsgReply();
        condition.setChannelId(reply.getChannelId());
        List<MsgReply> msgReplies = msgReplyMapper.selectMsgReplyList(condition);
        List<Long> topicMsgIds = new ArrayList<>();
        List<MsgReply> replyList =
                msgReplies.stream().filter(item -> {
                    MsgReplyExt replyExt = JSON.parseObject(StringUtils.defaultString(item.getExtData(), "{}"), MsgReplyExt.class);
                    List<Long> availableUsers = replyExt.getAvailableUsers();
                    Boolean matchFlag = CollectionUtils.isEmpty(availableUsers) || availableUsers.contains(loginUser.getUserid());
                    if (matchFlag) {
                        topicMsgIds.add(item.getId());
                    }
                    return matchFlag;
                }).collect(Collectors.toList());

        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>(Message.class);
        queryWrapper.in(Message::getId, topicMsgIds);
        queryWrapper.le(Message::getDeleteTime, -1L);
        List<Message> topicMessages = messageMapper.selectList(queryWrapper);
        List<MessageVo> messageVos = messageQueryService.processMetaData(topicMessages, null, null, null, null);

        Map<Long, MessageVo> messageMap = new HashMap<>();
        List<Long> rootIds = new ArrayList<>();

        messageVos.forEach(item -> {
            messageMap.put(item.getId(), item);
            rootIds.add(item.getId());
        });
        queryWrapper.clear();
        queryWrapper.select(Message::getId, Message::getRootId, Message::getPropsStr, Message::getMentionUsers);
        queryWrapper.in(Message::getRootId, rootIds);
        queryWrapper.le(Message::getDeleteTime, -1L);

        List<Message> topicMessageItems = messageMapper.selectList(queryWrapper);
        List<Long> mentionMeMsgIds = new ArrayList<>();
        List<Long> topicItemMsgIds = new ArrayList<>();
        Map<Long, Long> topicIdMap = new HashMap<>();
        topicMessageItems.forEach(item -> {
            topicItemMsgIds.add(item.getId());
            topicIdMap.put(item.getId(), item.getRootId());
            List<Long> mentionUsers = JSON.parseArray(StringUtils.defaultString(item.getMentionUsers()), Long.class);
            if (mentionUsers.contains(loginUser.getUserid())) {
                mentionMeMsgIds.add(item.getId());
            }
        });

        LambdaQueryWrapper<MsgUnread> unreadQueryWrapper = new LambdaQueryWrapper<>(MsgUnread.class);
        unreadQueryWrapper.eq(MsgUnread::getUserId, loginUser.getUserid());
        unreadQueryWrapper.in(MsgUnread::getMsgId, topicItemMsgIds);
        unreadQueryWrapper.isNull(MsgUnread::getReadTime);
        List<MsgUnread> unreadList = msgUnreadMapper.selectList(unreadQueryWrapper);

        Map<Long, Integer> unreadCountMap = new HashMap<>();
        Map<Long, Integer> unreadMentionCountMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(unreadList)) {
            unreadList.forEach(item -> {
                Long rootId = topicIdMap.get(item.getMsgId());
                Integer unreadCount = unreadCountMap.computeIfAbsent(rootId, key -> Integer.valueOf(0));
                unreadCount++;
                unreadCountMap.put(rootId, unreadCount);
                if (mentionMeMsgIds.contains(item.getMsgId())) {
                    Integer unreadMentionCount = unreadMentionCountMap.computeIfAbsent(rootId, key -> Integer.valueOf(0));
                    unreadMentionCount++;
                    unreadMentionCountMap.put(rootId, unreadMentionCount);
                }
            });
        }
        List<MsgReplyVo> replyVoList =
                replyList.stream().map(item -> {
                    MsgReplyVo vo = new MsgReplyVo();
                    BeanUtils.copyBeanProp(vo, item);
                    Integer unreadCount = unreadCountMap.get(item.getId());
                    unreadCount = unreadCount == null ? 0 : unreadCount;
                    vo.setUnreadCount(unreadCount);

                    Integer unreadMentionCount = unreadMentionCountMap.get(item.getId());
                    unreadMentionCount = unreadMentionCount == null ? 0 : unreadMentionCount;
                    vo.setMentionCount(unreadMentionCount);

                    Message message = messageMap.get(item.getId());
                    if (message != null) {
                        vo.setContent(message.getContent());
                    }
                    return vo;
                }).collect(Collectors.toList());


        return AjaxResult.success(replyVoList);
    }


    @Override
    public AjaxResult selectMentionMeTopicList(MsgReply reply) {
        if (reply == null) {
            return AjaxResult.error("查询话题列表失败，参数不能为空");
        }
        if (reply.getChannelId() == null) {
            return AjaxResult.error("查询话题列表失败，群组Id不能为空");
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("查询话题列表失败，获取用户信息失败");
        }

        MsgReply condition = new MsgReply();
        condition.setChannelId(reply.getChannelId());
        List<MsgReply> msgReplies = msgReplyMapper.selectMsgReplyList(condition);
        List<Long> topicMsgIds = new ArrayList<>();
        List<MsgReply> replyList =
                msgReplies.stream().filter(item -> {
                    MsgReplyExt replyExt = JSON.parseObject(StringUtils.defaultString(item.getExtData(), "{}"), MsgReplyExt.class);
                    List<Long> availableUsers = replyExt.getAvailableUsers();
                    Boolean matchFlag = CollectionUtils.isEmpty(availableUsers) || availableUsers.contains(loginUser.getUserid());
                    if (matchFlag) {
                        topicMsgIds.add(item.getId());
                    }
                    return matchFlag;
                }).collect(Collectors.toList());

        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>(Message.class);
        queryWrapper.in(Message::getId, topicMsgIds);
        queryWrapper.le(Message::getDeleteTime, -1L);
        List<Message> topicMessages = messageMapper.selectList(queryWrapper);
        Map<Long, Message> messageMap = new HashMap<>();
        List<Long> rootIds = new ArrayList<>();

        topicMessages.forEach(item -> {
            messageMap.put(item.getId(), item);
            rootIds.add(item.getId());
        });
        queryWrapper.clear();
        queryWrapper.select(Message::getId, Message::getRootId, Message::getPropsStr, Message::getMentionUsers);
        queryWrapper.in(Message::getRootId, rootIds);
        queryWrapper.le(Message::getDeleteTime, -1L);

        List<Message> topicMessageItems = messageMapper.selectList(queryWrapper);
        List<Long> mentionMeMsgIds = new ArrayList<>();
        List<Long> topicItemMsgIds = new ArrayList<>();
        Map<Long, Long> topicIdMap = new HashMap<>();
        topicMessageItems.forEach(item -> {
            topicItemMsgIds.add(item.getId());
            topicIdMap.put(item.getId(), item.getRootId());
            List<Long> mentionUsers = JSON.parseArray(StringUtils.defaultString(item.getMentionUsers()), Long.class);
            if (mentionUsers.contains(loginUser.getUserid())) {
                mentionMeMsgIds.add(item.getId());
            }
        });

        LambdaQueryWrapper<MsgUnread> unreadQueryWrapper = new LambdaQueryWrapper<>(MsgUnread.class);
        unreadQueryWrapper.eq(MsgUnread::getUserId, loginUser.getUserid());
        unreadQueryWrapper.in(MsgUnread::getMsgId, topicItemMsgIds);
        unreadQueryWrapper.isNull(MsgUnread::getReadTime);
        List<MsgUnread> unreadList = msgUnreadMapper.selectList(unreadQueryWrapper);

        Map<Long, Integer> unreadCountMap = new HashMap<>();
        Map<Long, Integer> unreadMentionCountMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(unreadList)) {
            unreadList.forEach(item -> {
                Long rootId = topicIdMap.get(item.getMsgId());
                Integer unreadCount = unreadCountMap.computeIfAbsent(rootId, key -> Integer.valueOf(0));
                unreadCount++;
                unreadCountMap.put(rootId, unreadCount);
                if (mentionMeMsgIds.contains(item.getMsgId())) {
                    Integer unreadMentionCount = unreadMentionCountMap.computeIfAbsent(rootId, key -> Integer.valueOf(0));
                    unreadMentionCount++;
                    unreadMentionCountMap.put(rootId, unreadMentionCount);
                }
            });
        }
        List<MsgReplyVo> replyVoList =
                replyList.stream().map(item -> {
                    MsgReplyVo vo = new MsgReplyVo();
                    BeanUtils.copyBeanProp(vo, item);
                    Integer unreadCount = unreadCountMap.get(item.getId());
                    unreadCount = unreadCount == null ? 0 : unreadCount;
                    vo.setUnreadCount(unreadCount);

                    Integer unreadMentionCount = unreadMentionCountMap.get(item.getId());
                    unreadMentionCount = unreadMentionCount == null ? 0 : unreadMentionCount;
                    vo.setMentionCount(unreadMentionCount);

                    Message message = messageMap.get(item.getId());
                    if (message != null) {
                        vo.setContent(message.getContent());
                    }
                    return vo;
                }).filter(item -> item.getMentionCount() > 0).collect(Collectors.toList());


        return AjaxResult.success(replyVoList);
    }

    @Override
    public AjaxResult selectMentionMeList(Message condition) {

        if (condition == null) {
            return AjaxResult.error("查询提及我的消息失败，参数不能为空");
        }
        if (condition.getChannelId() == null) {
            return AjaxResult.error("查询提及我的消息失败，群组Id不能为空");
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("查询提及我的消息失败，获取用户信息失败");
        }

        LambdaQueryWrapper<MsgUnread> unreadQueryWrapper = new LambdaQueryWrapper<>(MsgUnread.class);
        unreadQueryWrapper.eq(MsgUnread::getUserId, loginUser.getUserid());
        unreadQueryWrapper.in(MsgUnread::getChannelId, condition.getChannelId());
        unreadQueryWrapper.isNull(MsgUnread::getReadTime);
        List<MsgUnread> unreadList = msgUnreadMapper.selectList(unreadQueryWrapper);
        List<Long> msgIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(unreadList)) {
            msgIds.addAll(unreadList.stream().map(MsgUnread::getMsgId).collect(Collectors.toSet()));
        }

        List<Message> messages = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(msgIds)) {
            LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>(Message.class);
            queryWrapper.in(Message::getId, msgIds);
            queryWrapper.le(Message::getDeleteTime, -1L);
            messages.addAll(messageMapper.selectList(queryWrapper));
        }

        List<Message> mentionMeList = messages.stream().filter(item -> {
            List<Long> mentionUsers = JSON.parseArray(item.getMentionUsers(), Long.class);
            return mentionUsers.contains(loginUser.getUserid());
        }).collect(Collectors.toList());

        List<Long> mentionTopicMessageIds = new ArrayList<>(mentionMeList.stream().filter(item -> item.getRootId() != null).map(Message::getRootId).collect(Collectors.toSet()));
        List<Long> mentionMessageIds = new ArrayList<>(mentionMeList.stream().filter(item -> item.getRootId() == null && !mentionTopicMessageIds.contains(item.getId())).map(Message::getId).collect(Collectors.toSet()));


        List<MentionData> mentionDatas = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mentionTopicMessageIds)) {
            mentionTopicMessageIds.forEach(msgId -> {
                MentionData data = new MentionData();
                data.setMsgId(msgId);
                data.setTopicFlag(Boolean.TRUE);
                mentionDatas.add(data);
            });
        }

        if (CollectionUtils.isNotEmpty(mentionMessageIds)) {
            mentionMessageIds.forEach(msgId -> {
                MentionData data = new MentionData();
                data.setMsgId(msgId);
                data.setTopicFlag(Boolean.FALSE);
                mentionDatas.add(data);
            });
        }
        mentionDatas.sort(Comparator.comparing(MentionData::getMsgId));
        return AjaxResult.success(mentionDatas);
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
