package com.soflyit.chattask.im.message.service.impl;

import com.soflyit.chattask.im.message.domain.entity.MsgStatistic;
import com.soflyit.chattask.im.message.mapper.MsgStatisticMapper;
import com.soflyit.chattask.im.message.service.IMsgStatisticService;
import com.soflyit.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息读取统计Service业务层处理
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Service
public class MsgStatisticServiceImpl implements IMsgStatisticService {
    @Autowired
    private MsgStatisticMapper MsgStatisticMapper;


    @Override
    public MsgStatistic selectMsgStatisticByMsgId(Long msgId) {
        return MsgStatisticMapper.selectMsgStatisticByMsgId(msgId);
    }


    @Override
    public List<MsgStatistic> selectMsgStatisticList(MsgStatistic msgStatistic) {
        return MsgStatisticMapper.selectMsgStatisticList(msgStatistic);
    }


    @Override
    public int insertMsgStatistic(MsgStatistic msgStatistic) {
        msgStatistic.setCreateTime(DateUtils.getNowDate());
        return MsgStatisticMapper.insertMsgStatistic(msgStatistic);
    }


    @Override
    public int updateMsgStatistic(MsgStatistic msgStatistic) {
        msgStatistic.setUpdateTime(DateUtils.getNowDate());
        return MsgStatisticMapper.updateMsgStatistic(msgStatistic);
    }


    @Override
    public int deleteMsgStatisticByMsgIds(Long[] msgIds) {
        return MsgStatisticMapper.deleteMsgStatisticByMsgIds(msgIds);
    }


    @Override
    public int deleteMsgStatisticByMsgId(Long msgId) {
        return MsgStatisticMapper.deleteMsgStatisticByMsgId(msgId);
    }
}
