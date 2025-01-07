package com.soflyit.chattask.im.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.message.domain.entity.MsgReaction;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息回应Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
public interface MsgReactionMapper extends BaseMapper<MsgReaction> {

    MsgReaction selectMsgReactionById(Long id);


    List<MsgReaction> selectMsgReactionList(MsgReaction msgReaction);


    int insertMsgReaction(MsgReaction msgReaction);


    int updateMsgReaction(MsgReaction msgReaction);


    int deleteMsgReactionById(Long id);


    int deleteMsgReactionByIds(Long[] ids);
}
