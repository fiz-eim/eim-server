package com.soflyit.chattask.im.message.service;

import com.soflyit.chattask.im.message.domain.entity.MsgPinned;
import com.soflyit.common.core.web.domain.AjaxResult;

import java.util.List;

/**
 * 固定消息Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IMsgPinnedService {

    MsgPinned selectMsgPinnedById(Long id);


    List<MsgPinned> selectMsgPinnedList(MsgPinned msgPinned);


    int insertMsgPinned(MsgPinned msgPinned);


    int updateMsgPinned(MsgPinned msgPinned);


    int deleteMsgPinnedByIds(Long[] ids);


    int deleteMsgPinnedById(Long id);


    AjaxResult pinnedMessage(Long msgId);


    AjaxResult unpinnedMessage(Long msgId);


    AjaxResult getPinnedMessages(Long channelId);
}
