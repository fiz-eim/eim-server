package com.soflyit.chattask.im.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.im.event.domain.MessageReactionEvent;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.im.message.domain.entity.MsgReaction;
import com.soflyit.chattask.im.message.domain.vo.MessageMetadata;
import com.soflyit.chattask.im.message.domain.vo.MessageReactionEventData;
import com.soflyit.chattask.im.message.domain.vo.MessageReadVo;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.im.message.mapper.MsgReactionMapper;
import com.soflyit.chattask.im.message.service.IMsgReactionService;
import com.soflyit.chattask.im.system.service.impl.UserNickNameService;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import io.netty.channel.ChannelId;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息回应Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Service
public class MsgReactionServiceImpl implements IMsgReactionService {
    @Autowired
    private MsgReactionMapper msgReactionMapper;

    @Autowired
    private ChatEventService chatEventService;

    @Autowired
    private UserNickNameService userNickNameService;


    @Override
    public MsgReaction selectMsgReactionById(Long id) {
        return msgReactionMapper.selectMsgReactionById(id);
    }


    @Override
    public List<MsgReaction> selectMsgReactionList(MsgReaction msgReaction) {
        return msgReactionMapper.selectMsgReactionList(msgReaction);
    }


    @Override
    public int insertMsgReaction(MsgReaction msgReaction) {
        msgReaction.setCreateTime(DateUtils.getNowDate());
        return msgReactionMapper.insertMsgReaction(msgReaction);
    }


    @Override
    public int updateMsgReaction(MsgReaction msgReaction) {
        msgReaction.setUpdateTime(DateUtils.getNowDate());
        return msgReactionMapper.updateMsgReaction(msgReaction);
    }


    @Override
    public int deleteMsgReactionByIds(Long[] ids) {
        return msgReactionMapper.deleteMsgReactionByIds(ids);
    }


    @Override
    public int deleteMsgReactionById(Long id) {
        return msgReactionMapper.deleteMsgReactionById(id);
    }



    @Override
    public AjaxResult addReaction(MsgReaction msgReaction) {

        msgReaction.setId(SnowflakeIdUtil.nextId());
        msgReaction.setUserId(SecurityUtils.getUserId());
        msgReactionMapper.insertMsgReaction(msgReaction);
        LambdaQueryWrapper<MsgReaction> reactionWrapper = new LambdaQueryWrapper<MsgReaction>(MsgReaction.class);
        reactionWrapper.select(MsgReaction::getId, MsgReaction::getMsgId, MsgReaction::getChannelId, MsgReaction::getEmojiName, MsgReaction::getCreateTime, MsgReaction::getUserId);
        reactionWrapper.eq(MsgReaction::getMsgId, msgReaction.getMsgId());
        List<MsgReaction> reactions = msgReactionMapper.selectList(reactionWrapper);
        pushMessageReactionEvent(new ArrayList<>(reactions), msgReaction.getChannelId(), msgReaction.getMsgId());
        return AjaxResult.success(msgReaction);
    }


    @Override
    public AjaxResult removeReaction(Long id) {

        MsgReaction reaction = msgReactionMapper.selectMsgReactionById(id);
        if (reaction == null) {
            return AjaxResult.error("撤销回应失败， 回应不存在");
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("撤销回应失败， 获取用户信息失败");
        }
        if (!reaction.getUserId().equals(loginUser.getUserid())) {
            return AjaxResult.error("撤销回应失败， 权限不足");
        }

        msgReactionMapper.deleteMsgReactionById(id);

        LambdaQueryWrapper<MsgReaction> reactionWrapper = new LambdaQueryWrapper<>(MsgReaction.class);
        reactionWrapper.select(MsgReaction::getId, MsgReaction::getMsgId, MsgReaction::getChannelId, MsgReaction::getEmojiName, MsgReaction::getCreateTime, MsgReaction::getUserId);
        reactionWrapper.eq(MsgReaction::getMsgId, reaction.getMsgId());
        List<MsgReaction> reactions = msgReactionMapper.selectList(reactionWrapper);
        pushMessageReactionEvent(new ArrayList<>(reactions), reaction.getChannelId(), reaction.getMsgId());
        return AjaxResult.success();
    }


    @Override
    public void processReactionData(List<Long> messageIds, List<MessageVo> messageVoList) {

        LambdaQueryWrapper<MsgReaction> reactionWrapper = new LambdaQueryWrapper<>();
        reactionWrapper.select(MsgReaction::getId, MsgReaction::getMsgId, MsgReaction::getChannelId, MsgReaction::getEmojiName, MsgReaction::getCreateTime, MsgReaction::getUserId);
        reactionWrapper.in(MsgReaction::getMsgId, messageIds);
        List<MsgReaction> reactions = msgReactionMapper.selectList(reactionWrapper);
        if (CollectionUtils.isNotEmpty(reactions)) {
            Map<Long, List<MsgReaction>> reactionMap = reactions.stream().collect(Collectors.groupingBy(MsgReaction::getMsgId));

            messageVoList.forEach(item -> {
                MessageMetadata metadata = item.getMetaData();
                List<MsgReaction> msgReactions = reactionMap.get(item.getId());
                if (msgReactions == null) {
                    metadata.setReactions(new ArrayList<>());
                } else {
                    processReactionUserName(msgReactions);
                    metadata.setReactions(msgReactions);
                }
            });
        }
    }

    @Override
    public void processReactionData(MessageVo messageVo) {
        List<Long> messageIds = new ArrayList<>();
        List<MessageVo> messageVoList = new ArrayList<>();
        messageIds.add(messageVo.getId());
        messageVoList.add(messageVo);
        processReactionData(messageIds, messageVoList);
    }

    @Deprecated
    @Override
    public void processReactionData(MessageReadVo readVo) {
        MessageVo messageVo = new MessageVo();
        BeanUtils.copyBeanProp(messageVo, readVo);
        List<Long> messageIds = new ArrayList<>();
        List<MessageVo> messageVoList = new ArrayList<>();
        messageIds.add(messageVo.getId());
        messageVoList.add(messageVo);
        processReactionData(messageIds, messageVoList);
        readVo.getMetaData().setReactions(messageVo.getMetaData().getReactions());
    }

    private void processReactionUserName(List<MsgReaction> reactions) {
        Map<Long, List<MsgReaction>> reactionMap = reactions.stream().collect(Collectors.groupingBy(MsgReaction::getUserId));
        Map<Long, String> userNameMap = userNickNameService.getNickNameByIds(new ArrayList<>(reactionMap.keySet()));
        if (MapUtils.isNotEmpty(userNameMap)) {
            reactionMap.forEach((userId, reactionList) -> {
                String nickName = userNameMap.get(userId);
                reactionList.forEach(reaction -> {
                    reaction.setUserName(nickName);
                });
            });
        }

    }

    private void pushMessageReactionEvent(ArrayList<MsgReaction> reactions, Long channelId, Long msgId) {
        if (CollectionUtils.isNotEmpty(reactions)) {
            processReactionUserName(reactions);
        }


        MessageReactionEvent event = new MessageReactionEvent();

        MessageReactionEventData reactionEventData = new MessageReactionEventData();
        reactionEventData.setChannelId(channelId);
        reactionEventData.setMsgId(msgId);
        reactionEventData.setReactions(reactions);

        event.setData(reactionEventData);

        ChatBroadcast<Long, Long, ChannelId> messageBroadcast = new ChatBroadcast();
        messageBroadcast.setChannelId(channelId);
        event.setBroadcast(messageBroadcast);
        chatEventService.doProcessEvent(event);
    }


}
