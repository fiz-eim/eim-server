package com.soflyit.chattask.im.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.im.message.domain.entity.MsgFavorite;
import com.soflyit.common.core.web.domain.AjaxResult;

public interface MsgFavoriteService extends IService<MsgFavorite> {

    AjaxResult getFavoriteMessages();

    AjaxResult addFavorite(Long msgId);

    AjaxResult deleteFavorite(Long favoriteId);
}
