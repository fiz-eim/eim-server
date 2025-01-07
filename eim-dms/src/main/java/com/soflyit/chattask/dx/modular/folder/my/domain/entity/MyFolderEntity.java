package com.soflyit.chattask.dx.modular.folder.my.domain.entity;

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
 * 我的文档对象 dx_my_folder
 *
 * @author jiaozhishang
 * @date 2023-11-10 14:06:13
 */
@TableName(value = "dx_my_folder")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class MyFolderEntity extends DmsCommonEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "主键")
    @TableId(value = "ID")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "目录ID")
    @TableField(value = "FOLDER_ID")
    @ApiModelProperty("目录ID")
    private Long folderId;


    @Excel(name = "用户ID")
    @TableField(value = "USER_ID")
    @ApiModelProperty("用户ID")
    private String userId;



}
