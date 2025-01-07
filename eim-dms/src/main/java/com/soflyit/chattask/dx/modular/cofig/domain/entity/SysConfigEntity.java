package com.soflyit.chattask.dx.modular.cofig.domain.entity;

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
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.cofig.domain
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-09  19:36
 * @Description: xxxEntity 命名为与表结构存在对应关系的封装，为 Dao层数据结构。
 * @Version: 1.0
 */
@TableName(value = "dx_sys_config")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfigEntity extends DmsCommonEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "主键")
    @TableId(value = "sys_config_id")
    @ApiModelProperty("主键")
    private Long sysConfigId;


    @Excel(name = "配置项")
    @TableField(value = "config_key")
    @ApiModelProperty("配置项")
    private String configKey;


    @Excel(name = "配置信息状态;1.正常；-1.未启用等")
    @TableField(value = "config_status")
    @ApiModelProperty("配置信息状态;1.正常；-1.未启用等")
    private Long configStatus;


    @Excel(name = "配置值")
    @TableField(value = "config_value")
    @ApiModelProperty("配置值")
    private String configValue;

}
