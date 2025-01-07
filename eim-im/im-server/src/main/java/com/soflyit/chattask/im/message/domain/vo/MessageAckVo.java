package com.soflyit.chattask.im.message.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.soflyit.chattask.im.message.domain.entity.Message;
import lombok.Data;

import java.util.Date;

/**
 * 消息确认信息<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-06 16:59
 */
@Data
public class MessageAckVo extends Message {


    private Long ackBy;


    private String ackType;


    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date ackTime;


    private String ackUser;

}
