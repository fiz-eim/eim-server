package com.soflyit.chattask.im.channel.service.impl;

import com.soflyit.chattask.im.channel.domain.entity.HisChannelMember;
import com.soflyit.chattask.im.channel.mapper.HisChannelMemberMapper;
import com.soflyit.chattask.im.channel.service.IHisChannelMemberService;
import com.soflyit.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 频道成员历史Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Service
public class HisChannelMemberServiceImpl implements IHisChannelMemberService {
    @Autowired
    private HisChannelMemberMapper hisChannelMemberMapper;


    @Override
    public HisChannelMember selectHisChannelMemberById(Long id) {
        return hisChannelMemberMapper.selectHisChannelMemberById(id);
    }


    @Override
    public List<HisChannelMember> selectHisChannelMemberList(HisChannelMember hisChannelMember) {
        return hisChannelMemberMapper.selectHisChannelMemberList(hisChannelMember);
    }


    @Override
    public int insertHisChannelMember(HisChannelMember hisChannelMember) {
        hisChannelMember.setCreateTime(DateUtils.getNowDate());
        return hisChannelMemberMapper.insertHisChannelMember(hisChannelMember);
    }


    @Override
    public int updateHisChannelMember(HisChannelMember hisChannelMember) {
        hisChannelMember.setUpdateTime(DateUtils.getNowDate());
        return hisChannelMemberMapper.updateHisChannelMember(hisChannelMember);
    }


    @Override
    public int deleteHisChannelMemberByIds(Long[] ids) {
        return hisChannelMemberMapper.deleteHisChannelMemberByIds(ids);
    }


    @Override
    public int deleteHisChannelMemberById(Long id) {
        return hisChannelMemberMapper.deleteHisChannelMemberById(id);
    }
}
