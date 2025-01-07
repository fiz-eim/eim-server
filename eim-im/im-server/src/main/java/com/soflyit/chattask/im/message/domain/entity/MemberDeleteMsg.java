package com.soflyit.chattask.im.message.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@TableName(value = "member_delete_msg")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class MemberDeleteMsg extends BaseEntity {

    @TableId
    private Long id;

    private Long memberId;

    private Long channelId;

    private Long msgId;

    private Long userId;

    private Date deleteTime;


}
