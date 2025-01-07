package com.soflyit.chattask.im.system.service.impl;

import com.soflyit.chattask.im.system.domain.ChatUserStatus;
import com.soflyit.chattask.im.system.mapper.ChatUserStatusMapper;
import com.soflyit.chattask.im.system.service.IChatUserStatusService;
import com.soflyit.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户在线状态Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Service
public class ChatUserStatusServiceImpl implements IChatUserStatusService {
    @Autowired
    private ChatUserStatusMapper chatUserStatusMapper;


    @Override
    public ChatUserStatus selectChatUserStatusById(Long id) {
        return chatUserStatusMapper.selectChatUserStatusById(id);
    }


    @Override
    public List<ChatUserStatus> selectChatUserStatusList(ChatUserStatus chatUserStatus) {
        return chatUserStatusMapper.selectChatUserStatusList(chatUserStatus);
    }


    @Override
    public int insertChatUserStatus(ChatUserStatus chatUserStatus) {
        chatUserStatus.setCreateTime(DateUtils.getNowDate());
        return chatUserStatusMapper.insertChatUserStatus(chatUserStatus);
    }


    @Override
    public int updateChatUserStatus(ChatUserStatus chatUserStatus) {
        chatUserStatus.setUpdateTime(DateUtils.getNowDate());
        return chatUserStatusMapper.updateChatUserStatus(chatUserStatus);
    }


    @Override
    public int deleteChatUserStatusByIds(Long[] ids) {
        return chatUserStatusMapper.deleteChatUserStatusByIds(ids);
    }


    @Override
    public int deleteChatUserStatusById(Long id) {
        return chatUserStatusMapper.deleteChatUserStatusById(id);
    }
}
