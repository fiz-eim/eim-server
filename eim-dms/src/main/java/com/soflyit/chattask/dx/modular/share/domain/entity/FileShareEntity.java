package com.soflyit.chattask.dx.modular.share.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soflyit.chattask.dx.common.base.DmsCommonEntity;
import com.soflyit.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件分享对象 dx_file_share
 *
 * @author soflyit
 * @date 2023-11-07 16:50:36
 */
@TableName(value = "dx_file_share")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class FileShareEntity extends DmsCommonEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "目录ID")
    @TableField(value = "folder_id")
    @ApiModelProperty("目录ID")
    private String folderId;


    @Excel(name = "资源ID")
    @TableField(value = "resource_id")
    @ApiModelProperty("资源ID")
    private String resourceId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;


    @Excel(name = "分享截止时间;为空时，可不限时长分享；不为空进行时长限定")
    @TableField(value = "share_end_time")
    @ApiModelProperty("分享截止时间;为空时，可不限时长分享；不为空进行时长限定")
    private String shareEndTime;


    @Excel(name = "主键;分享主键ID")
    @TableId(value = "share_id")
    @ApiModelProperty("主键;分享主键ID")
    private Long shareId;


    @Excel(name = "分享需要密码验证;为空时，不需要；不为空则需要")
    @TableField(value = "share_pass")
    @ApiModelProperty("分享需要密码验证;为空时，不需要；不为空则需要")
    private String sharePass;


    @Excel(name = "分享内容的状态;0.正常;-1.过期；-2：文件不存在等")
    @TableField(value = "share_status")
    @ApiModelProperty("分享内容的状态;0.正常;-1.过期；-2：文件不存在等")
    private String shareStatus;


    @Excel(name = "分享主题")
    @TableField(value = "share_title")
    @ApiModelProperty("分享主题")
    private String shareTitle;


    @Excel(name = "分享类型;folder:目录；file:文件等")
    @TableField(value = "share_type")
    @ApiModelProperty("分享类型;folder:目录；file:文件等")
    private String shareType;


}
