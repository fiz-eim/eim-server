package com.soflyit.chattask.dx.modular.folder.organization.domain.entity;

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
 * 文档库存储目录对象 dx_folder
 *
 * @author soflyit
 * @date 2023-11-08 14:54:35
 */
@TableName(value = "dx_folder")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class FolderEntity extends DmsCommonEntity {
    private static final long serialVersionUID = 1L;



    @Excel(name = "主键")
    @TableId(value = "FOLDER_ID")
    @ApiModelProperty("主键")
    private Long folderId;

    @Excel(name = "主键")
    @TableField(value = "FOLDER_FlAG")
    @ApiModelProperty("主键")
    private String folderFlag;


    @Excel(name = "文件数;目录中文件的统计数：是否有必要维护？")
    @TableField(value = "FILE_TOTAL")
    @ApiModelProperty("文件数;目录中文件的统计数：是否有必要维护？")
    private Long fileTotal;


    @Excel(name = "层级;格式：[FOLDER_ID1,FOLDER_ID2,FOLDER_ID3,FOLDER_ID4]")
    @TableField(value = "FOLDER_LEVEL")
    @ApiModelProperty("层级;格式：[FOLDER_ID1,FOLDER_ID2,FOLDER_ID3,FOLDER_ID4]")
    private String folderLevel;


    @Excel(name = "目录名称")
    @TableField(value = "FOLDER_NAME")
    @ApiModelProperty("目录名称")
    private String folderName;


    @Excel(name = "上级目录ID;默认规范 ： 0为顶级目录")
    @TableField(value = "FOLDER_PARENT_ID")
    @ApiModelProperty("上级目录ID;默认规范 ： 0为顶级目录")
    private Long folderParentId;


    @Excel(name = "目录数;子级目录统计数：是否有必要维护？")
    @TableField(value = "FOLDER_TOTAL")
    @ApiModelProperty("目录数;子级目录统计数：是否有必要维护？")
    private Long folderTotal;
}
