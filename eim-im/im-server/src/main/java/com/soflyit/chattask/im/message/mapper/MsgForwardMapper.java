package com.soflyit.chattask.im.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.message.domain.entity.MsgForward;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息转发Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
public interface MsgForwardMapper extends BaseMapper<MsgForward> {

    MsgForward selectMsgForwardById(Long id);


    List<MsgForward> selectMsgForwardList(MsgForward msgForward);


    int insertMsgForward(MsgForward msgForward);


    int updateMsgForward(MsgForward msgForward);


    int deleteMsgForwardById(Long id);


    int deleteMsgForwardByIds(Long[] ids);
}
