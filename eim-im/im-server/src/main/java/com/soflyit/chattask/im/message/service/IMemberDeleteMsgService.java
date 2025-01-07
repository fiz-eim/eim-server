package com.soflyit.chattask.im.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.im.message.domain.entity.MemberDeleteMsg;
import com.soflyit.common.core.web.domain.AjaxResult;

import java.util.List;

public interface IMemberDeleteMsgService extends IService<MemberDeleteMsg> {
    AjaxResult deleteMessageById(Long id, List<Long> msgIds);

}
