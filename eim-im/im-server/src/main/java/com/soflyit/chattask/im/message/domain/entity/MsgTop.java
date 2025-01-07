package com.soflyit.chattask.im.message.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("msg_top")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgTop extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    private Long channelId;

    private Long msgId;

    private String closeUsers;

    private Long userId;

    private Long cardId;

    private Long cardAppId;

    private String cardDefine;

    private String cardData;


}
