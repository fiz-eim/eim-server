package com.soflyit.chattask.im.channel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 频道Mapper接口
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@Repository
@Mapper
public interface ChatChannelMapper extends BaseMapper<ChatChannel> {

    ChatChannel selectChatChannelById(Long id);


    List<ChatChannel> selectChatChannelList(ChatChannel chatChannel);


    int insertChatChannel(ChatChannel chatChannel);


    int updateChatChannel(ChatChannel chatChannel);


    int updateChatChannelRemark(ChatChannel chatChannel);


    int deleteChatChannelById(Long id);


    int deleteChatChannelByIds(Long[] ids);


    List<ChatChannel> selectChannelByUserId(ChatChannel condition);
}
