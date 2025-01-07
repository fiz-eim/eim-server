package com.soflyit.chattask.dx.modular.share.domain.param;

import com.soflyit.chattask.dx.common.base.OrderByParam;
import com.soflyit.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件分享对象 dx_file_share
 *
 * @author soflyit
 * @date 2023-11-07 16:50:36
 */
@ApiModel
@Data
public class FileShareQueryParam implements Serializable {
    private static final long serialVersionUID = 1L;


    @Excel(name = "删除标记")
    @ApiModelProperty("删除标记")
    private Integer deleteFlag;


    @ApiModelProperty("目录ID")
    private String folderId;


    @ApiModelProperty("资源ID")
    private String resourceId;


    @ApiModelProperty("乐观锁")
    private Long revision;


    @ApiModelProperty("分享截止时间;为空时，可不限时长分享；不为空进行时长限定")
    private String shareEndTime;


    @ApiModelProperty("主键;分享主键ID")
    private Long shareId;


    @ApiModelProperty("分享需要密码验证;为空时，不需要；不为空则需要")
    private String sharePass;


    @ApiModelProperty("分享内容的状态;0.正常;-1.过期；-2：文件不存在等")
    private String shareStatus;


    @ApiModelProperty("分享主题")
    private OrderByParam<String> shareTitle;


    @ApiModelProperty("分享类型;folder:目录；file:文件等")
    private String shareType;


    @ApiModelProperty("租户号")
    private Long tenantId;
}
