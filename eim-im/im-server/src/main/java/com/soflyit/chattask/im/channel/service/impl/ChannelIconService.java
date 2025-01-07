package com.soflyit.chattask.im.channel.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.domain.param.ChannelAddParam;
import com.soflyit.chattask.im.channel.domain.vo.ChannelExtData;
import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;
import com.soflyit.chattask.im.channel.mapper.ChannelMemberMapper;
import com.soflyit.chattask.im.channel.mapper.ChatChannelMapper;
import com.soflyit.chattask.im.channel.service.IChatChannelService;
import com.soflyit.chattask.im.channel.task.GenerateChannelIconTask;
import com.soflyit.chattask.im.config.SoflyImFileConfig;
import com.soflyit.chattask.im.event.domain.ChannelUpdateEvent;
import com.soflyit.chattask.im.event.service.ChatEventService;
import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.RemoteAvatarApi;
import com.soflyit.system.api.model.LoginUser;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.soflyit.chattask.im.common.constant.ChannelConstant.CHANNEL_TYPE_DIRECT;
import static com.soflyit.chattask.im.common.constant.ChannelConstant.CHANNEL_TYPE_DISCUSSION;
import static com.soflyit.common.core.constant.SecurityConstants.INNER;

/**
 * 频道头像服务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-02-01 18:05
 */
@Component
@Slf4j
public class ChannelIconService {

    private RemoteAvatarApi remoteAvatarApi;

    private SoflyImFileConfig soflyImfileConfig;

    private ChatChannelMapper chatChannelMapper;

    private ChannelMemberMapper channelMemberMapper;

    private ThreadPoolTaskExecutor taskExecutor;

    private ChatEventService chatEventService;

    private IChatChannelService chatChannelService;


    public void processChannelIcon(ChannelAddParam chatChannel, List<ChannelMember> members) {
        LoginUser user = SecurityUtils.getLoginUser();
        Integer channelType = chatChannel.getType();


        if (CHANNEL_TYPE_DISCUSSION.equals(channelType)) {
            GenerateChannelIconTask task = new GenerateChannelIconTask(chatChannel, members, remoteAvatarApi, soflyImfileConfig, (item) -> {
                log.debug("更新频道[{}]logo:{}", item.getId(), item.getIcon());

                if (StringUtils.isNotEmpty(item.getIcon())) {
                    ChatChannel channelInDB = chatChannelMapper.selectChatChannelById(item.getId());
                    channelInDB.setIcon(item.getIcon());
                    chatChannelMapper.updateChatChannel(channelInDB);
                    processEdit(channelInDB, user);
                }
            });
            taskExecutor.execute(task);
        } else if (CHANNEL_TYPE_DIRECT.equals(channelType)) {

            ChatChannel channelInDB = chatChannelMapper.selectChatChannelById(chatChannel.getId());
            processEdit(channelInDB, user);
        }
    }


    private void processEdit(ChatChannel channel, LoginUser user) {

        ChannelVo channelVo = new ChannelVo();
        BeanUtils.copyBeanProp(channelVo, channel);
        channelVo.setExt(JSON.parseObject(StringUtils.defaultString(channel.getExtData(), "{}"), ChannelExtData.class));


        List<ChannelVo> channelVos = new ArrayList<>();
        channelVos.add(channelVo);

        if (CHANNEL_TYPE_DIRECT.equals(channel.getType())) {
            chatChannelService.processDirectChannelName(channelVos, user);
        }
        processChannelIconForQuery(channelVos, user);
        if (channelVo.getMsgTops() == null) {
            channelVo.setMsgTops(new ArrayList<>());
        }
        ChannelUpdateEvent channelUpdateEvent = new ChannelUpdateEvent();
        channelUpdateEvent.setData(channelVo);

        ChatBroadcast<Long, Long, ChannelId> broadcast = new ChatBroadcast<>();
        broadcast.setChannelId(channel.getId());
        broadcast.setOmitUsers(new HashMap<>());
        channelUpdateEvent.setBroadcast(broadcast);

        chatEventService.doProcessEvent(channelUpdateEvent);
    }

    public void processChannelIconForQuery(List<ChannelVo> channelList, LoginUser user) {
        List<ChatChannel> directChannels = new ArrayList<>();
        Map<String, List<ChatChannel>> channelMap = new HashMap<>();

        channelList.forEach(item -> {
            if (CHANNEL_TYPE_DIRECT.equals(item.getType())) {
                directChannels.add(item);
            } else if (StringUtils.isNotEmpty(item.getIcon())) {
                List<ChatChannel> iconChannels = channelMap.computeIfAbsent(item.getIcon(), k -> new ArrayList<>());
                iconChannels.add(item);
            }
        });

        if (MapUtils.isNotEmpty(channelMap)) {
            R<Map<String, String>> pathMapResult = remoteAvatarApi.getAvatarFullPaths(new ArrayList<>(channelMap.keySet()), INNER);
            if (pathMapResult != null && pathMapResult.getCode() == R.SUCCESS) {
                Map<String, String> pathMap = pathMapResult.getData();
                channelMap.forEach((key, val) -> {
                    String fullPath = pathMap.get(key);
                    val.forEach(vItem -> {
                        vItem.setIcon(fullPath);
                    });
                });
            }
        }


        if (CollectionUtils.isNotEmpty(directChannels)) {
            Map<Long, ChatChannel> channelIdMap = directChannels.stream().collect(Collectors.toMap(ChatChannel::getId, item -> item));
            LambdaQueryWrapper<ChannelMember> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(ChannelMember::getUserId, ChannelMember::getChannelId);
            queryWrapper.in(ChannelMember::getChannelId, channelIdMap.keySet());
            queryWrapper.ne(ChannelMember::getUserId, user.getUserid());
            List<ChannelMember> members = channelMemberMapper.selectList(queryWrapper);
            Map<Long, Long> channelMemberMap = members.stream().collect(Collectors.toMap(ChannelMember::getUserId, ChannelMember::getChannelId, (v1, v2) -> v2));

            R<Map<Long, String>> userIdMapResult = remoteAvatarApi.getUserAvatarByUserIds(new ArrayList<>(channelMemberMap.keySet()), INNER);
            if (userIdMapResult != null && userIdMapResult.getCode() == R.SUCCESS) {
                Map<Long, String> userIdMap = userIdMapResult.getData();
                channelMemberMap.forEach((k, v) -> {
                    ChatChannel channel = channelIdMap.get(v);
                    if (channel != null) {
                        channel.setIcon(userIdMap.get(k));
                    }
                });
            }
        }
    }

    @Autowired
    public void setSoflyImfileConfig(SoflyImFileConfig soflyImfileConfig) {
        this.soflyImfileConfig = soflyImfileConfig;
    }

    @Autowired
    public void setRemoteAvatarApi(RemoteAvatarApi remoteAvatarApi) {
        this.remoteAvatarApi = remoteAvatarApi;
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
    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Autowired
    public void setChatEventService(ChatEventService chatEventService) {
        this.chatEventService = chatEventService;
    }

    @Lazy
    @Autowired
    public void setChatChannelService(IChatChannelService chatChannelService) {
        this.chatChannelService = chatChannelService;
    }
}
