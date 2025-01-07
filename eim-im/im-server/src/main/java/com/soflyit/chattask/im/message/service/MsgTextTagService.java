package com.soflyit.chattask.im.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.im.message.domain.entity.MsgTextTag;
import com.soflyit.chattask.im.message.domain.param.MsgTextTagAddParam;
import com.soflyit.common.core.web.domain.AjaxResult;

public interface MsgTextTagService extends IService<MsgTextTag> {
    AjaxResult addTag(MsgTextTagAddParam textTag);

    AjaxResult deleteTag(MsgTextTagAddParam textTag);
}
