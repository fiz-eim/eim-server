package com.soflyit.chattask.im.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.message.domain.entity.MemberDeleteMsg;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.im.message.mapper.MemberDeleteMsgMapper;
import com.soflyit.chattask.im.message.mapper.MessageMapper;
import com.soflyit.chattask.im.message.service.IMemberDeleteMsgService;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 频道成员删除频道消息
 */
@Service
public class MemberDeleteMsgImpl extends ServiceImpl<MemberDeleteMsgMapper, MemberDeleteMsg> implements IMemberDeleteMsgService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessagePushService messagePushService;

    @Autowired
    private ChannelMemberMapper channelMemberMapper;

    @Override
    public AjaxResult deleteMessageById(Long msgId, List<Long> msgIds) {
        if (msgId == null && CollectionUtils.isEmpty(msgIds)) {
            return AjaxResult.error("删除消息失败：消息Id不能为空");
        }

        Message message = messageMapper.selectMessageById(msgId);
        if (message == null) {
            return AjaxResult.error("删除消息失败：消息不存在");
        } else if (message.getDeleteTime() > -1L) {
            MessageVo messageVo = new MessageVo();
            BeanUtils.copyBeanProp(messageVo, message);
            return AjaxResult.success(messageVo);
        }
        Long userId = SecurityUtils.getUserId();
        Long channelId = message.getChannelId();

        LambdaQueryWrapper<ChannelMember> memberQueryWrapper = new LambdaQueryWrapper();
        memberQueryWrapper.select(ChannelMember::getId);
        memberQueryWrapper.eq(ChannelMember::getChannelId, channelId);
        memberQueryWrapper.eq(ChannelMember::getUserId, userId);
        List<ChannelMember> members = channelMemberMapper.selectList(memberQueryWrapper);
        if (CollectionUtils.isEmpty(members)) {
            return AjaxResult.error("删除消息失败：仅本频道成员可以删除消息");
        }
        ChannelMember member = members.get(0);

        MemberDeleteMsg memberDeleteMsg = new MemberDeleteMsg();
        memberDeleteMsg.setId(SnowflakeIdUtil.nextId());
        memberDeleteMsg.setChannelId(message.getChannelId());
        memberDeleteMsg.setUserId(userId);
        memberDeleteMsg.setMsgId(msgId);
        memberDeleteMsg.setMemberId(member.getId());
        memberDeleteMsg.setDeleteTime(new Date());
        save(memberDeleteMsg);

        MessageVo messageVo = new MessageVo();
        BeanUtils.copyBeanProp(messageVo, message);

        messagePushService.pushMemberDeleteMsgEvent(messageVo, userId);

        return AjaxResult.success(message);
    }
}
