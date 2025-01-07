package com.soflyit.chattask.dx.modular.resource.path.domain.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soflyit.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文档目录安全映射对象 dx_resource_path
 *
 * @author soflyit
 * @date 2023-11-07 16:56:53
 */
@ApiModel
@Data
public class ResourcePathQueryParam implements Serializable {
    private static final long serialVersionUID = 1L;


    @Excel(name = "创建人")
    @TableField(value = "CREATED_BY")
    @ApiModelProperty("创建人")
    private Long createdBy;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(value = "CREATED_TIME")
    @ApiModelProperty("创建时间")
    private Date createdTime;


    @Excel(name = "目录ID;下载时，通过验证，如果需要下载源文件，通过resource_id 查找")
    @TableField(value = "folder_id")
    @ApiModelProperty("目录ID;下载时，通过验证，如果需要下载源文件，通过resource_id 查找")
    private String folderId;


    @Excel(name = "资源ID;资源文件ID")
    @TableField(value = "resource_id")
    @ApiModelProperty("资源ID;资源文件ID")
    private String resourceId;


    @Excel(name = "主键")
    @TableField(value = "resource_path_id")
    @ApiModelProperty("主键")
    private Long resourcePathId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;


    @Excel(name = "安全验证串;再分享时，生成的验证串")
    @TableField(value = "safe_verify_path")
    @ApiModelProperty("安全验证串;再分享时，生成的验证串")
    private String safeVerifyPath;


    @Excel(name = "分享ID")
    @TableField(value = "share_id")
    @ApiModelProperty("分享ID")
    private String shareId;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "更新人")
    @TableField(value = "UPDATED_BY")
    @ApiModelProperty("更新人")
    private Long updatedBy;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(value = "UPDATED_TIME")
    @ApiModelProperty("更新时间")
    private Date updatedTime;

}
