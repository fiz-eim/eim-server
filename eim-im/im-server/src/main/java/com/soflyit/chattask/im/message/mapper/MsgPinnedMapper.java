package com.soflyit.chattask.im.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.message.domain.entity.MsgPinned;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 固定消息Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
public interface MsgPinnedMapper extends BaseMapper<MsgPinned> {

    MsgPinned selectMsgPinnedById(Long id);


    List<MsgPinned> selectMsgPinnedList(MsgPinned msgPinned);


    int insertMsgPinned(MsgPinned msgPinned);


    int updateMsgPinned(MsgPinned msgPinned);


    int deleteMsgPinnedById(Long id);


    int deleteMsgPinnedByIds(Long[] ids);
}
