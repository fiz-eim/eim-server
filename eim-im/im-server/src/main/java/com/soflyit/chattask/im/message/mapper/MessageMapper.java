package com.soflyit.chattask.im.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.param.MessageQueryParam;
import com.soflyit.chattask.im.message.domain.param.UserMessageQueryParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
public interface MessageMapper extends BaseMapper<Message> {

    Message selectMessageById(Long id);


    List<Message> selectMessageList(Message message);


    int insertMessage(Message message);


    int updateMessage(Message message);


    int deleteMessageById(Long id);


    int deleteMessageByIds(Long[] ids);


    List<Message> selectMessageListByParam(MessageQueryParam message);



    List<Message> selectUserMessageListByParam(UserMessageQueryParam param);
}
