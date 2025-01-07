package com.soflyit.chattask.dx.modular.share.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件分享对象 dx_file_share
 *
 * @author soflyit
 * @date 2023-11-07 16:50:36
 */
@ApiModel
@Data
public class FileShareAddParam {


    @ApiModelProperty("资源ID")
    private String resourceId;



    @ApiModelProperty("分享截止时间;为空时，可不限时长分享；不为空进行时长限定")
    private String shareEndTime;




    @ApiModelProperty("分享需要密码验证;为空时，不需要；不为空则需要")
    private String sharePass;



    @ApiModelProperty("分享内容的状态;0.正常;-1.过期；-2：文件不存在等")
    private String shareStatus;



    @ApiModelProperty("分享主题")
    private String shareTitle;
}
