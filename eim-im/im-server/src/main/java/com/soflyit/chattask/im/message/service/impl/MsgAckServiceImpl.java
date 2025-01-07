package com.soflyit.chattask.im.message.service.impl;

import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgAck;
import com.soflyit.chattask.im.message.mapper.MsgAckMapper;
import com.soflyit.chattask.im.message.service.IMsgAckService;
import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 消息确认Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Service
public class MsgAckServiceImpl implements IMsgAckService {

    private MsgAckMapper msgAckMapper;


    @Override
    public MsgAck selectMsgAckById(Long id) {
        return msgAckMapper.selectMsgAckById(id);
    }


    @Override
    public List<MsgAck> selectMsgAckList(MsgAck msgAck) {
        return msgAckMapper.selectMsgAckList(msgAck);
    }


    @Override
    public int insertMsgAck(MsgAck msgAck) {
        msgAck.setCreateTime(DateUtils.getNowDate());
        return msgAckMapper.insertMsgAck(msgAck);
    }


    @Override
    public int updateMsgAck(MsgAck msgAck) {
        msgAck.setUpdateTime(DateUtils.getNowDate());
        return msgAckMapper.updateMsgAck(msgAck);
    }


    @Override
    public int deleteMsgAckByIds(Long[] ids) {
        return msgAckMapper.deleteMsgAckByIds(ids);
    }


    @Override
    public int deleteMsgAckById(Long id) {
        return msgAckMapper.deleteMsgAckById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult ackMessage(Message message, Long userId) {
        return AjaxResult.error("暂不持支");
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult cancelAckMessage(Message message, Long userId) {
        return AjaxResult.error("暂不支持");
    }

    @Autowired
    public void setMsgAckMapper(MsgAckMapper msgAckMapper) {
        this.msgAckMapper = msgAckMapper;
    }

}
