package com.soflyit.chattask.im.channel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.param.MemberChangeNotifyParam;
import com.soflyit.chattask.im.channel.domain.param.MemberRemoveParam;
import com.soflyit.chattask.im.channel.domain.vo.MemberCountVo;
import com.soflyit.chattask.im.channel.domain.vo.MemberVo;
import com.soflyit.common.core.web.domain.AjaxResult;

import java.util.List;

/**
 * 频道成员Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:18
 */
public interface IChannelMemberService {

    ChannelMember selectChannelMemberById(Long id);


    List<MemberVo> selectChannelMemberList(ChannelMember channelMember);


    int insertChannelMember(ChannelMember channelMember);


    int updateChannelMember(ChannelMember channelMember);


    int deleteChannelMemberByIds(Long[] ids);


    int deleteChannelMemberById(Long id);


    AjaxResult<MemberCountVo> getMemberCount(Long channelId);


    AjaxResult<MemberVo> getMemberInfo(ChannelMember condition);


    AjaxResult removeMember(MemberRemoveParam condition);


    AjaxResult leaveChannel(Long channelId);


    AjaxResult<List<ChannelMember>> addChannelMember(List<ChannelMember> channelMembers, Long channelId, Boolean checkBusinessFlag);


    AjaxResult<ChannelMember> updateNotify(Long channelId, Long userId, MemberChangeNotifyParam param);


    ChannelMember getUnreadCount(Long userId, Long channelId);


    AjaxResult changeRole(ChannelMember member);

    AjaxResult pinChannel(ChannelMember channelMember);

    AjaxResult dndChannel(ChannelMember channelMember);


    AjaxResult addManagers(List<ChannelMember> members, Long channelId);

    AjaxResult deleteManager(ChannelMember member, Long channelId);

    AjaxResult changeOwner(ChannelMember member, Long channelId);

    AjaxResult changeChannelDisplayName(ChannelMember channelMember);


    AjaxResult updateMemberNickName(ChannelMember channelMember);


    AjaxResult collapseChannel(ChannelMember channelMember);
}
