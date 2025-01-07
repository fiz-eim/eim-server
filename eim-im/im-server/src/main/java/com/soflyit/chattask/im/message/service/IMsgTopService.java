package com.soflyit.chattask.im.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;
import com.soflyit.chattask.im.message.domain.entity.MsgTop;

import java.util.List;

public interface IMsgTopService extends IService<MsgTop> {
    void processChannelTopMsg(List<Long> channelIdList, List<ChannelVo> result);
}
