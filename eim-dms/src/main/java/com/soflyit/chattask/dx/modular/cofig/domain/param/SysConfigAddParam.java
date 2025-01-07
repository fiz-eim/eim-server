package com.soflyit.chattask.dx.modular.cofig.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.cofig.domain
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-09  19:36
 * @Description: xxxParam 命名为API接口参数封装，为接收前端参数数据结构。
 * @Version: 1.0
 */
@ApiModel
@Data
public class SysConfigAddParam implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("配置项")
    private String configKey;


    @ApiModelProperty("配置信息状态;1.正常；-1.未启用等")
    private Long configStatus;


    @ApiModelProperty("配置值")
    private String configValue;
}
