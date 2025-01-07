package com.soflyit.chattask.im.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.channel.mapper.ChatChannelMapper;
import com.soflyit.chattask.im.channel.service.IChatChannelService;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgFavorite;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.im.message.domain.vo.MsgFavoriteVo;
import com.soflyit.chattask.im.message.mapper.MessageMapper;
import com.soflyit.chattask.im.message.mapper.MsgFavoriteMapper;
import com.soflyit.chattask.im.message.service.MsgFavoriteService;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.id.util.SnowflakeIdUtil;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.soflyit.chattask.im.common.constant.ChannelConstant.CHANNEL_TYPE_DIRECT;

/**
 * 消息收藏
 */
@Service
public class MsgFavoriteServiceImpl extends ServiceImpl<MsgFavoriteMapper, MsgFavorite> implements MsgFavoriteService {

    @Autowired
    private ChatChannelMapper chatChannelMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageQueryService messageQueryService;

    @Autowired
    private ChannelMemberMapper channelMemberMapper;

    @Autowired
    private IChatChannelService chatChannelService;


    @Override
    public AjaxResult getFavoriteMessages() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("查询收藏消息失败：无法获取当前用户信息");
        }


        LambdaQueryWrapper<MsgFavorite> queryWrapper = new LambdaQueryWrapper<>(MsgFavorite.class);
        queryWrapper.eq(MsgFavorite::getUserId, loginUser.getUserid());
        queryWrapper.orderByDesc(MsgFavorite::getId);

        List<MsgFavorite> favorites = getBaseMapper().selectList(queryWrapper);

        if (CollectionUtils.isNotEmpty(favorites)) {
            List<Long> msgIds = new ArrayList<>();
            Set<Long> channelIds = new HashSet<>();
            favorites.forEach(item -> {
                msgIds.add(item.getMsgId());
                channelIds.add(item.getChannelId());
            });

            LambdaQueryWrapper<ChatChannel> channelQueryWrapper = new LambdaQueryWrapper<>();
            channelQueryWrapper.select(ChatChannel::getId, ChatChannel::getName, ChatChannel::getType);
            channelQueryWrapper.in(ChatChannel::getId, channelIds);
            List<ChatChannel> chatChannels = chatChannelMapper.selectList(channelQueryWrapper);

            List<ChannelVo> directChannels = new ArrayList<>();

            Map<Long, ChannelVo> channelMap = chatChannels.stream().collect(Collectors.toMap(ChatChannel::getId, val -> {
                ChannelVo channelVo = new ChannelVo();
                BeanUtils.copyBeanProp(channelVo, val);
                if (CHANNEL_TYPE_DIRECT.equals(val.getType())) {
                    directChannels.add(channelVo);
                }
                return channelVo;
            }, (v1, v2) -> v1));

            if (CollectionUtils.isNotEmpty(directChannels)) {
                chatChannelService.processDirectChannelName(directChannels, loginUser);
            }

            LambdaQueryWrapper<Message> messageQueryWrapper = new LambdaQueryWrapper<>(Message.class);
            messageQueryWrapper.in(Message::getId, msgIds);
            List<Message> messageList = messageMapper.selectList(messageQueryWrapper);
            List<MessageVo> messageVos = messageQueryService.processMetaData(messageList, msgIds, null, null, null);

            Map<Long, MessageVo> messageVoMap = messageVos.stream().collect(Collectors.toMap(Message::getId, item -> item, (v1, v2) -> v1));

            List<MsgFavoriteVo> favoriteVos = new ArrayList<>();
            favorites.forEach(item -> {
                MsgFavoriteVo favoriteVo = new MsgFavoriteVo();
                BeanUtils.copyBeanProp(favoriteVo, item);
                favoriteVos.add(favoriteVo);

                favoriteVo.setChannel(channelMap.get(item.getChannelId()));
                favoriteVo.setMessage(messageVoMap.get(item.getMsgId()));
            });
            return AjaxResult.success(favoriteVos);

        }

        return AjaxResult.success(new ArrayList<>());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addFavorite(Long msgId) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        Message message = messageMapper.selectMessageById(msgId);

        if (message == null) {
            String msg = "消息收藏失败：消息不存在";
            return AjaxResult.error(msg);
        }

        if (message.getDeleteTime() > 0) {
            String msg = "消息收藏失败：消息已删除";
            return AjaxResult.error(msg);
        }

        ChannelMember member = new ChannelMember();
        member.setUserId(loginUser.getUserid());
        member.setChannelId(message.getChannelId());
        List<ChannelMember> channelMembers = channelMemberMapper.selectChannelMemberList(member);
        if (CollectionUtils.isEmpty(channelMembers)) {
            String msg = "收藏消息失败：没有权限";
            return AjaxResult.error(msg);
        }


        LambdaQueryWrapper<MsgFavorite> favoriteQueryWrapper = new LambdaQueryWrapper<>(MsgFavorite.class);
        favoriteQueryWrapper.eq(MsgFavorite::getMsgId, msgId);
        favoriteQueryWrapper.eq(MsgFavorite::getUserId, loginUser.getUserid());
        List<MsgFavorite> existDatas = getBaseMapper().selectList(favoriteQueryWrapper);
        if (CollectionUtils.isNotEmpty(existDatas)) {
            return AjaxResult.success();
        }

        MsgFavorite msgFavorite = new MsgFavorite();
        msgFavorite.setId(SnowflakeIdUtil.nextId());
        msgFavorite.setUserId(loginUser.getUserid());
        msgFavorite.setChannelId(member.getChannelId());
        msgFavorite.setMsgId(msgId);
        msgFavorite.setFavoriteTime(new Date());
        getBaseMapper().insert(msgFavorite);
        return AjaxResult.success(msgFavorite);
    }


    @Override
    public AjaxResult deleteFavorite(Long favoriteId) {

        LoginUser loginUser = SecurityUtils.getLoginUser();

        MsgFavorite msgFavorite = getBaseMapper().selectById(favoriteId);
        if (msgFavorite != null) {
            if (!msgFavorite.getUserId().equals(loginUser.getUserid())) {
                return AjaxResult.error("删除收藏失败：权限不足");
            }
        } else {
            return AjaxResult.success();
        }
        getBaseMapper().deleteById(favoriteId);
        return AjaxResult.success();
    }


}
