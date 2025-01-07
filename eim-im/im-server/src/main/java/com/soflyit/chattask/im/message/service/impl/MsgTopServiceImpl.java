package com.soflyit.chattask.im.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgTop;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.im.message.domain.vo.MsgTopVo;
import com.soflyit.chattask.im.message.mapper.MessageMapper;
import com.soflyit.chattask.im.message.mapper.MsgTopMapper;
import com.soflyit.chattask.im.message.service.IMsgTopService;
import com.soflyit.chattask.im.system.service.impl.UserNickNameService;
import com.soflyit.common.core.utils.bean.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MsgTopServiceImpl extends ServiceImpl<MsgTopMapper, MsgTop> implements IMsgTopService {

    @Autowired
    private MessageMapper messageMapper;


    @Autowired
    private MessageQueryService messageQueryService;

    @Autowired
    private UserNickNameService userNickNameService;

    @Override
    public void processChannelTopMsg(List<Long> channelIdList, List<ChannelVo> result) {

        if (CollectionUtils.isEmpty(channelIdList)) {
            return;
        }
        LambdaQueryWrapper<MsgTop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MsgTop::getChannelId, channelIdList);
        List<MsgTop> topList = getBaseMapper().selectList(queryWrapper);

        if (CollectionUtils.isEmpty(topList)) {
            result.forEach(item -> {
                item.setMsgTops(new ArrayList<>());
            });
            return;
        }
        List<Long> messageIds = topList.stream().map(MsgTop::getMsgId).collect(Collectors.toList());
        LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>();
        messageQueryWrapper.in(Message::getId, messageIds);
        List<Message> topMessages = messageMapper.selectList(messageQueryWrapper);
        if (CollectionUtils.isEmpty(topMessages)) {
            result.forEach(item -> {
                item.setMsgTops(new ArrayList<>());
            });
            return;
        }

        Set<Long> userIds = topMessages.stream().map(Message::getUserId).collect(Collectors.toSet());
        userIds.addAll(topList.stream().map(MsgTop::getUserId).collect(Collectors.toSet()));

        Map<Long, String> userIdNameMap = userNickNameService.getNickNameByIds(new ArrayList<>(userIds));


        List<MessageVo> messageVos = messageQueryService.processMetaData(topMessages, messageIds, null, null, null);

        Map<Long, MessageVo> channelMsgMap = messageVos.stream().collect(Collectors.toMap(Message::getId, val -> val, (v1, v2) -> v1));


        Map<Long, List<MsgTopVo>> msgTopMap = new HashMap<>();
        topList.forEach(item -> {
            MsgTopVo topVo = new MsgTopVo();
            BeanUtils.copyBeanProp(topVo, item);
            topVo.setMessageData(channelMsgMap.get(item.getMsgId()));
            List<MsgTopVo> tops = msgTopMap.computeIfAbsent(item.getChannelId(), key -> new ArrayList<>());
            topVo.setCreateUser(userIdNameMap.get(topVo.getUserId()));
            tops.add(topVo);
        });

        result.forEach(channel -> {
            List<MsgTopVo> tops = msgTopMap.get(channel.getId());
            if (tops == null) {
                tops = new ArrayList<>();
            }
            channel.setMsgTops(tops);
        });

    }
}
