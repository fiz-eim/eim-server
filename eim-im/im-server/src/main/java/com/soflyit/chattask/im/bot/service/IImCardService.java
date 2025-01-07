package com.soflyit.chattask.im.bot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.im.channel.domain.entity.ImCard;
import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;

import java.util.List;

public interface IImCardService extends IService<ImCard> {
    void processTopCard(List<Long> channelIdList, List<ChannelVo> result);

}
