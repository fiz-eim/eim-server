package com.soflyit.chattask.im.message.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("msg_text_tag")
public class MsgTextTag extends BaseEntity {

    @TableId
    private Long msgId;

    @TableField("tag_data")
    private String tagData;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;


}
