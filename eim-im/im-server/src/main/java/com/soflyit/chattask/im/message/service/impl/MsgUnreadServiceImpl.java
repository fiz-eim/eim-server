package com.soflyit.chattask.im.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgUnread;
import com.soflyit.chattask.im.message.domain.vo.sysdata.RecallMessageData;
import com.soflyit.chattask.im.message.mapper.MsgUnreadMapper;
import com.soflyit.chattask.im.message.service.IMsgUnreadService;
import com.soflyit.common.core.utils.DateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息未读明细Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Service
public class MsgUnreadServiceImpl extends ServiceImpl<MsgUnreadMapper, MsgUnread> implements IMsgUnreadService {

    private ChannelMemberMapper channelMemberMapper;


    @Override
    public MsgUnread selectMsgUnreadById(Long id) {
        return getBaseMapper().selectMsgUnreadById(id);
    }


    @Override
    public List<MsgUnread> selectMsgUnreadList(MsgUnread msgUnread) {
        return getBaseMapper().selectMsgUnreadList(msgUnread);
    }


    @Override
    public int insertMsgUnread(MsgUnread msgUnread) {
        msgUnread.setCreateTime(DateUtils.getNowDate());
        return getBaseMapper().insertMsgUnread(msgUnread);
    }


    @Override
    public int updateMsgUnread(MsgUnread msgUnread) {
        msgUnread.setUpdateTime(DateUtils.getNowDate());
        return getBaseMapper().updateMsgUnread(msgUnread);
    }


    @Override
    public int deleteMsgUnreadByIds(Long[] ids) {
        return getBaseMapper().deleteMsgUnreadByIds(ids);
    }


    @Override
    public int deleteMsgUnreadById(Long id) {
        return getBaseMapper().deleteMsgUnreadById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void processRecallMessageUnread(RecallMessageData messageData) {
        Message message = messageData.getDeletedMessage();
        MsgUnread condition = new MsgUnread();
        condition.setMsgId(message.getId());
        List<MsgUnread> msgReadList = getBaseMapper().selectMsgUnreadList(condition);
        List<MsgUnread> msgUnreadList = null;
        if (CollectionUtils.isNotEmpty(msgReadList)) {
            msgUnreadList = msgReadList.stream().filter(item -> item.getReadTime() == null).collect(Collectors.toList());
        }
        List<Long> userIds = new ArrayList<>();
        List<Long> msgUnreadIdList = new ArrayList<>();


        if (CollectionUtils.isNotEmpty(msgUnreadList)) {
            msgUnreadList.forEach(item -> {
                userIds.add(item.getUserId());
                msgUnreadIdList.add(item.getId());
            });

            LambdaUpdateWrapper<MsgUnread> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(MsgUnread::getReadTime, new Date());
            updateWrapper.set(MsgUnread::getRemark, "message recall");
            updateWrapper.in(MsgUnread::getId, msgUnreadIdList);
            getBaseMapper().update(null, updateWrapper);
        }

        if (CollectionUtils.isNotEmpty(userIds)) {

            Long channelId = message.getChannelId();

            LambdaQueryWrapper<MsgUnread> unreadMsgQueryWrapper = new LambdaQueryWrapper<>(MsgUnread.class);
            unreadMsgQueryWrapper.in(MsgUnread::getUserId, userIds);
            unreadMsgQueryWrapper.eq(MsgUnread::getChannelId, channelId);
            unreadMsgQueryWrapper.isNull(MsgUnread::getReadTime);

            List<MsgUnread> msgUnreadCountList = getBaseMapper().selectList(unreadMsgQueryWrapper);
            Map<Long, Long> unreadMap = msgUnreadCountList.stream().collect(Collectors.groupingBy(item -> item.getUserId(), Collectors.counting()));

            LambdaQueryWrapper<ChannelMember> queryWrapper = new LambdaQueryWrapper<>(ChannelMember.class);
            queryWrapper.eq(ChannelMember::getChannelId, channelId);

            List<ChannelMember> members = channelMemberMapper.selectList(queryWrapper);
            ChannelMember updateEntity = new ChannelMember();

            members.forEach(item -> {
                Long unreadCount = unreadMap.get(item.getUserId());
                updateEntity.setId(item.getId());
                updateEntity.setUnreadCount(unreadCount == null ? 0L : unreadCount);
                channelMemberMapper.updateChannelMember(updateEntity);
            });
        }
    }

    @Autowired
    public void setChannelMemberMapper(ChannelMemberMapper channelMemberMapper) {
        this.channelMemberMapper = channelMemberMapper;
    }
}
