package com.soflyit.chattask.im.message.service;

import com.soflyit.chattask.im.message.domain.entity.MsgForward;
import com.soflyit.chattask.im.message.domain.param.MessageForwardParam;
import com.soflyit.common.core.web.domain.AjaxResult;

import java.util.List;

/**
 * 消息转发Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IMsgForwardService {

    MsgForward selectMsgForwardById(Long id);


    List<MsgForward> selectMsgForwardList(MsgForward msgForward);


    int insertMsgForward(MsgForward msgForward);


    int updateMsgForward(MsgForward msgForward);


    int deleteMsgForwardByIds(Long[] ids);


    int deleteMsgForwardById(Long id);


    AjaxResult forwardMessage(MessageForwardParam forwardParam);
}
