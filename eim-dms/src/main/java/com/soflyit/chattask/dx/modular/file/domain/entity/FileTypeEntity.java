package com.soflyit.chattask.dx.modular.file.domain.entity;

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
 * 文件类型 dx_file_type
 *
 * @author dddgoal@163.com
 * @date 2023-11-10 14:43:36
 */
@TableName(value = "dx_file_type")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class FileTypeEntity extends DmsCommonEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "主键;表主键ID")
    @TableId(value = "file_type_id")
    @ApiModelProperty("主键;表主键ID")
    private Long fileTypeId;


    @Excel(name = "文件分类")
    @TableField(value = "file_type_key")
    @ApiModelProperty("文件分类")
    private String fileTypeKey;


    @Excel(name = "文件描述")
    @TableField(value = "file_type_name")
    @ApiModelProperty("文件描述")
    private String fileTypeName;


    @Excel(name = "文件类型细分")
    @TableField(value = "file_type_value")
    @ApiModelProperty("文件类型细分")
    private String fileTypeValue;



    @Excel(name = "备注")
    @TableField(value = "remark")
    @ApiModelProperty("备注")
    private String remark;

}
