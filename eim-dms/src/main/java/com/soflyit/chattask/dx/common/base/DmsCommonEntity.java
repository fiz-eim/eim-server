package com.soflyit.chattask.dx.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class DmsCommonEntity implements Serializable {


    @TableLogic(value = "2", delval = "1")
    @ApiModelProperty(value = "删除标志")
    @TableField(value = "delete_flag",fill = FieldFill.INSERT)
    private Integer deleteFlag;


    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;


    @ApiModelProperty(value = "创建人")
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private Long createBy;


    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;


    @ApiModelProperty(value = "更新人")
    @TableField(value = "update_by",fill = FieldFill.UPDATE)
    private Long updateBy;


    @ApiModelProperty("租户号")
    private Long tenantId;


    @ApiModelProperty("乐观锁")
    private Long revision;
}
