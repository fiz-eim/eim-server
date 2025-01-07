package com.soflyit.chattask.im.message.service;

import com.soflyit.chattask.im.message.domain.entity.RefReplyMember;

import java.util.List;

/**
 * 回复消息与用户关系Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IRefReplyMemberService {

    RefReplyMember selectRefReplyMemberById(Long id);


    List<RefReplyMember> selectRefReplyMemberList(RefReplyMember refReplyMember);


    int insertRefReplyMember(RefReplyMember refReplyMember);


    int updateRefReplyMember(RefReplyMember refReplyMember);


    int deleteRefReplyMemberByIds(Long[] ids);


    int deleteRefReplyMemberById(Long id);
}
