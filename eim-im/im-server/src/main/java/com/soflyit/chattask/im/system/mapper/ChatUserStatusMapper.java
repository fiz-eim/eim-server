package com.soflyit.chattask.im.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.system.domain.ChatUserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户在线状态Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
public interface ChatUserStatusMapper extends BaseMapper<ChatUserStatus> {

    ChatUserStatus selectChatUserStatusById(Long id);


    List<ChatUserStatus> selectChatUserStatusList(ChatUserStatus chatUserStatus);


    int insertChatUserStatus(ChatUserStatus chatUserStatus);


    int updateChatUserStatus(ChatUserStatus chatUserStatus);


    int deleteChatUserStatusById(Long id);


    int deleteChatUserStatusByIds(Long[] ids);
}
