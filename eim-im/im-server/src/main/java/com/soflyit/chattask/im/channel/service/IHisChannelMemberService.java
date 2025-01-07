package com.soflyit.chattask.im.channel.service;

import com.soflyit.chattask.im.channel.domain.entity.HisChannelMember;

import java.util.List;

/**
 * 频道成员历史Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IHisChannelMemberService {

    HisChannelMember selectHisChannelMemberById(Long id);


    List<HisChannelMember> selectHisChannelMemberList(HisChannelMember hisChannelMember);


    int insertHisChannelMember(HisChannelMember hisChannelMember);


    int updateHisChannelMember(HisChannelMember hisChannelMember);


    int deleteHisChannelMemberByIds(Long[] ids);


    int deleteHisChannelMemberById(Long id);
}
