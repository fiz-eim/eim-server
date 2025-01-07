package com.soflyit.chattask.im.system.service;

import com.soflyit.chattask.im.system.domain.ChatUserStatus;

import java.util.List;

/**
 * 用户在线状态Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IChatUserStatusService {

    ChatUserStatus selectChatUserStatusById(Long id);


    List<ChatUserStatus> selectChatUserStatusList(ChatUserStatus chatUserStatus);


    int insertChatUserStatus(ChatUserStatus chatUserStatus);


    int updateChatUserStatus(ChatUserStatus chatUserStatus);


    int deleteChatUserStatusByIds(Long[] ids);


    int deleteChatUserStatusById(Long id);
}
