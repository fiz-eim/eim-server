package com.soflyit.chattask.im.channel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 频道成员Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:18
 */
@Repository
public interface ChannelMemberMapper extends BaseMapper<ChannelMember> {

    ChannelMember selectChannelMemberById(Long id);


    List<ChannelMember> selectChannelMemberList(ChannelMember channelMember);


    int insertChannelMember(ChannelMember channelMember);


    int updateChannelMember(ChannelMember channelMember);


    int deleteChannelMemberById(Long id);


    int deleteChannelMemberByIds(Long[] ids);


    void batchInsert(List<ChannelMember> members);


    List<ChannelMember> selectDirectChannelIdByUserId(Long userId);


    List<ChannelMember> selectDirectChannelMembers(List<Long> channelIds);


    List<ChannelMember> selectChannelByUserId(Long userId);

}
