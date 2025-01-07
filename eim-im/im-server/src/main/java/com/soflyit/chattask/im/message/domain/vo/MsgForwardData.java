package com.soflyit.chattask.im.message.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 转发消息数据 <br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-02 15:38
 */
@Data
public class MsgForwardData {


    private Boolean forwardFlag = Boolean.FALSE;


    private List<MessageVo> messageList;


}
