package com.soflyit.chattask.im.bot.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.im.bot.mapper.ImCardMapper;
import com.soflyit.chattask.im.bot.service.IImCardService;
import com.soflyit.chattask.im.channel.domain.entity.ImCard;
import com.soflyit.chattask.im.channel.domain.param.CardData;
import com.soflyit.chattask.im.channel.domain.param.ImCardVo;
import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;
import com.soflyit.chattask.im.message.service.IMessageService;
import com.soflyit.chattask.im.message.service.impl.MessagePushService;
import com.soflyit.common.core.utils.bean.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.soflyit.chattask.im.common.constant.ImCardConstant.CARD_MSG_FLAG_FALSE;
import static com.soflyit.chattask.im.common.constant.ImCardConstant.CARD_PINNED_TRUE;

@Service
public class ImCardServiceImpl extends ServiceImpl<ImCardMapper, ImCard> implements IImCardService {


    @Autowired
    private MessagePushService messagePushService;


    @Autowired
    private IMessageService messageService;


    @Override
    public void processTopCard(List<Long> channelList, List<ChannelVo> result) {

        LambdaQueryWrapper<ImCard> queryWrapper = new LambdaQueryWrapper<>(ImCard.class);
        queryWrapper.eq(ImCard::getPinnedFlag, CARD_PINNED_TRUE);
        queryWrapper.eq(ImCard::getMsgCardFlag, CARD_MSG_FLAG_FALSE);
        queryWrapper.in(ImCard::getChannelId, channelList);

        List<ImCard> imCards = getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(imCards)) {
            List<ImCardVo> cardVos = new ArrayList<>();
            imCards.forEach(item -> {
                ImCardVo vo = new ImCardVo();
                BeanUtils.copyBeanProp(vo, item);
                vo.setData(JSON.parseObject(item.getCardData(), CardData.class));
                cardVos.add(vo);
            });

            Map<Long, List<ImCardVo>> cardMap = cardVos.stream().collect(Collectors.groupingBy(ImCard::getChannelId));
            result.forEach(item -> {
                item.setTopCards(cardMap.get(item.getId()));
            });
        }

    }

}
