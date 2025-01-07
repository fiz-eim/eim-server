package com.soflyit.chattask.im.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.message.domain.entity.RefReplyMember;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 回复消息与用户关系Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
public interface RefReplyMemberMapper extends BaseMapper<RefReplyMember> {

    RefReplyMember selectRefReplyMemberById(Long id);


    List<RefReplyMember> selectRefReplyMemberList(RefReplyMember refReplyMember);


    int insertRefReplyMember(RefReplyMember refReplyMember);


    int updateRefReplyMember(RefReplyMember refReplyMember);


    int deleteRefReplyMemberById(Long id);


    int deleteRefReplyMemberByIds(Long[] ids);
}
