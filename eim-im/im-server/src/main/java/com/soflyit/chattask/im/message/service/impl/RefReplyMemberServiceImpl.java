package com.soflyit.chattask.im.message.service.impl;

import com.soflyit.chattask.im.message.domain.entity.RefReplyMember;
import com.soflyit.chattask.im.message.mapper.RefReplyMemberMapper;
import com.soflyit.chattask.im.message.service.IRefReplyMemberService;
import com.soflyit.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 回复消息与用户关系Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Service
public class RefReplyMemberServiceImpl implements IRefReplyMemberService {
    @Autowired
    private RefReplyMemberMapper refReplyMemberMapper;


    @Override
    public RefReplyMember selectRefReplyMemberById(Long id) {
        return refReplyMemberMapper.selectRefReplyMemberById(id);
    }


    @Override
    public List<RefReplyMember> selectRefReplyMemberList(RefReplyMember refReplyMember) {
        return refReplyMemberMapper.selectRefReplyMemberList(refReplyMember);
    }


    @Override
    public int insertRefReplyMember(RefReplyMember refReplyMember) {
        refReplyMember.setCreateTime(DateUtils.getNowDate());
        return refReplyMemberMapper.insertRefReplyMember(refReplyMember);
    }


    @Override
    public int updateRefReplyMember(RefReplyMember refReplyMember) {
        refReplyMember.setUpdateTime(DateUtils.getNowDate());
        return refReplyMemberMapper.updateRefReplyMember(refReplyMember);
    }


    @Override
    public int deleteRefReplyMemberByIds(Long[] ids) {
        return refReplyMemberMapper.deleteRefReplyMemberByIds(ids);
    }


    @Override
    public int deleteRefReplyMemberById(Long id) {
        return refReplyMemberMapper.deleteRefReplyMemberById(id);
    }
}
