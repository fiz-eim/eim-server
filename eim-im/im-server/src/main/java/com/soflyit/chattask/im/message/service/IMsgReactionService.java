package com.soflyit.chattask.im.message.service;

import com.soflyit.chattask.im.message.domain.entity.MsgReaction;
import com.soflyit.chattask.im.message.domain.vo.MessageReadVo;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.common.core.web.domain.AjaxResult;

import java.util.List;

/**
 * 消息回应Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IMsgReactionService {

    public MsgReaction selectMsgReactionById(Long id);


    public List<MsgReaction> selectMsgReactionList(MsgReaction msgReaction);


    public int insertMsgReaction(MsgReaction msgReaction);


    public int updateMsgReaction(MsgReaction msgReaction);


    public int deleteMsgReactionByIds(Long[] ids);


    public int deleteMsgReactionById(Long id);

    AjaxResult addReaction(MsgReaction msgReaction);

    AjaxResult removeReaction(Long id);

    void processReactionData(List<Long> allMessageIds, List<MessageVo> messageVoList);

    void processReactionData(MessageVo messageVo);


    void processReactionData(MessageReadVo messageVo);

}
