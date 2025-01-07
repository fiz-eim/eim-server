package com.soflyit.chattask.dx.modular.resource.path.domain.vo;

import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文档目录安全映射对象 dx_resource_path
 *
 * @author soflyit
 * @date 2023-11-07 16:56:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourcePathVO extends BaseEntity {
    private static final long serialVersionUID = 1L;
    

    @Excel(name = "目录ID;下载时，通过验证，如果需要下载源文件，通过resource_id 查找")
    @ApiModelProperty("目录ID;下载时，通过验证，如果需要下载源文件，通过resource_id 查找")
    private String folderId;


    @Excel(name = "资源ID;资源文件ID")
    @ApiModelProperty("资源ID;资源文件ID")
    private String resourceId;


    @Excel(name = "主键")
    @ApiModelProperty("主键")
    private Long resourcePathId;


    @Excel(name = "乐观锁")
    @ApiModelProperty("乐观锁")
    private Long revision;


    @Excel(name = "安全验证串;再分享时，生成的验证串")
    @ApiModelProperty("安全验证串;再分享时，生成的验证串")
    private String safeVerifyPath;


    @Excel(name = "分享ID")
    @ApiModelProperty("分享ID")
    private String shareId;


    @Excel(name = "租户号")
    @ApiModelProperty("租户号")
    private Long tenantId;



}
