package com.soflyit.chattask.im.channel.service;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.domain.param.ChannelAddParam;
import com.soflyit.chattask.im.channel.domain.param.ChannelQueryParam;
import com.soflyit.chattask.im.channel.domain.param.UserChannelQueryParam;
import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;
import com.soflyit.chattask.im.channel.domain.vo.DirectChannelVo;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.system.api.model.LoginUser;

import java.util.List;

/**
 * 频道Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IChatChannelService {

    ChatChannel selectChatChannelById(Long id);


    List<ChatChannel> selectChatChannelList(ChannelQueryParam chatChannel);


    ChatChannel insertChatChannel(ChannelAddParam chatChannel);


    AjaxResult updateChatChannel(ChatChannel chatChannel);


    int deleteChatChannelByIds(Long[] ids);


    int deleteChatChannelById(Long id);


    List<ChannelVo> selectUserChannelList(UserChannelQueryParam channelCondition);


    List<DirectChannelVo> selectUserDirectChannelList(UserChannelQueryParam chatChannel, LoginUser loginUser);


    ChannelVo selectUserChannel(Long userId, Long channelId);


    List<ChannelMember> selectUserMemberList(Long userId, Boolean includeDeleted);

    List<ChatChannel> selectChatChannelListByDelete();


    List<ChatChannel> getPinnedChannel(Long userId, Integer type);


    AjaxResult deleteChatChannel(Long id);


    AjaxResult<ChatChannel> restoreChatChannel(Long id);


    AjaxResult getChannelInfo(Long id);


    void createFolder(Long channelId);



    void processDirectChannelName(List<ChannelVo> directChannelList, LoginUser user);


    AjaxResult updateChannelRemark(ChatChannel chatChannel);


    AjaxResult editChannelConfig(ChannelVo channelVo);
}
