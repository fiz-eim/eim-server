package com.soflyit.chattask.im.bot.service;

import com.soflyit.chattask.im.channel.domain.param.CardData;
import com.soflyit.system.api.model.LoginUser;

/**
 * 卡片
 */
public interface CardDataHandler<T> {

    @SuppressWarnings("SameReturnValue")
    default T processData(CardData data, LoginUser currentUser) {
        return null;
    }

}
