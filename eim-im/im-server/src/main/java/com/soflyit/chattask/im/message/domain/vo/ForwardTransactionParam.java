package com.soflyit.chattask.im.message.domain.vo;

import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.param.ForwardChannelInfo;
import com.soflyit.chattask.im.message.domain.param.MessageForwardParam;
import com.soflyit.system.api.model.LoginUser;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 消息转发参数<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-02-21 16:34
 */
@Data
public class ForwardTransactionParam {

    private LoginUser loginUser;

    private Map<Integer, List<ForwardChannelInfo>> channalMap;

    private List<Message> messages;

    private String realName;

    private MessageForwardParam forwardParam;
}
