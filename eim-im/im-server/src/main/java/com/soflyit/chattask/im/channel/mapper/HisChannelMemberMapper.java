package com.soflyit.chattask.im.channel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.channel.domain.entity.HisChannelMember;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 频道成员历史Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
public interface HisChannelMemberMapper extends BaseMapper<HisChannelMember> {

    HisChannelMember selectHisChannelMemberById(Long id);


    List<HisChannelMember> selectHisChannelMemberList(HisChannelMember hisChannelMember);


    int insertHisChannelMember(HisChannelMember hisChannelMember);


    int updateHisChannelMember(HisChannelMember hisChannelMember);


    int deleteHisChannelMemberById(Long id);


    int deleteHisChannelMemberByIds(Long[] ids);
}
