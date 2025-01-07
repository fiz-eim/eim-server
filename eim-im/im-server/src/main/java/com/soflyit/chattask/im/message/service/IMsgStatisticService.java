package com.soflyit.chattask.im.message.service;

import com.soflyit.chattask.im.message.domain.entity.MsgStatistic;

import java.util.List;

/**
 * 消息读取统计Service接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
public interface IMsgStatisticService {

    MsgStatistic selectMsgStatisticByMsgId(Long msgId);


    List<MsgStatistic> selectMsgStatisticList(MsgStatistic msgStatistic);


    int insertMsgStatistic(MsgStatistic msgStatistic);


    int updateMsgStatistic(MsgStatistic msgStatistic);


    int deleteMsgStatisticByMsgIds(Long[] msgIds);


    int deleteMsgStatisticByMsgId(Long msgId);
}
