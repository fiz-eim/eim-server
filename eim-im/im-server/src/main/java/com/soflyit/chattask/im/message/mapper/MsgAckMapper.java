package com.soflyit.chattask.im.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.message.domain.entity.MsgAck;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息确认Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
public interface MsgAckMapper extends BaseMapper<MsgAck> {

    MsgAck selectMsgAckById(Long id);


    List<MsgAck> selectMsgAckList(MsgAck msgAck);


    int insertMsgAck(MsgAck msgAck);


    int updateMsgAck(MsgAck msgAck);


    int deleteMsgAckById(Long id);


    int deleteMsgAckByIds(Long[] ids);


    void batchInsert(List<MsgAck> unAckList);
}
