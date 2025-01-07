package com.soflyit.chattask.im.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.message.domain.entity.MsgStatistic;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息读取统计Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
public interface MsgStatisticMapper extends BaseMapper<MsgStatistic> {

    MsgStatistic selectMsgStatisticByMsgId(Long msgId);


    List<MsgStatistic> selectMsgStatisticList(MsgStatistic msgStatistic);


    int insertMsgStatistic(MsgStatistic msgStatistic);


    int updateMsgStatistic(MsgStatistic msgStatistic);


    int deleteMsgStatisticByMsgId(Long msgId);


    int deleteMsgStatisticByMsgIds(Long[] msgIds);
}
