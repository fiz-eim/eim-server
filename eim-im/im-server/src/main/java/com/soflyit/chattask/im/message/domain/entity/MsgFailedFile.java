package com.soflyit.chattask.im.message.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 失败文件对象 msg_failed_file
 *
 * @author soflyit
 * @date 2024-01-10 13:52:41
 */
@TableName(value = "msg_failed_file")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgFailedFile extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "消息id")
    @TableField(value = "msg_id")
    @ApiModelProperty("消息id")
    private Long msgId;


    @Excel(name = "文件Id")
    @TableField(value = "file_id")
    @ApiModelProperty("文件Id")
    private Long fileId;


    @Excel(name = "文件路径")
    @TableField(value = "file_path")
    @ApiModelProperty("文件路径")
    private String filePath;


    @Excel(name = "客户端文件id")
    @TableField(value = "uuid")
    @ApiModelProperty("客户端文件id")
    private String uuid;


    @Excel(name = "失败次数")
    @TableField(value = "failed_count")
    @ApiModelProperty("失败次数")
    private Integer failedCount;


    @Excel(name = "状态")
    @TableField(value = "status")
    @ApiModelProperty("状态")
    private Short status;


    @TableField(value = "file_type")
    @ApiModelProperty("文件类型")
    private Short fileType;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;

}
