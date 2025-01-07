package com.soflyit.chattask.dx.modular.resource.resource.domain.entity;

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
 * 系统资源文档库对象 dx_resource
 *
 * @author soflyit
 * @date 2023-11-07 16:56:57
 */
@TableName(value = "dx_resource")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceEntity extends DmsCommonEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "资源所属目录")
    @TableField(value = "FOLDER_PARENT_ID")
    @ApiModelProperty("资源所属目录ID,上级目录")
    private Long folderParentId;


    @Excel(name = "文档库存储ID;dx_folder 表 FOLDER_ID 字段")
    @TableField(value = "FOLDER_ID")
    @ApiModelProperty("文档库存储ID;dx_folder 表 FOLDER_ID 字段")
    private Long folderId;


    @Excel(name = "MD5加密串;在生成路径验证时，生成验证串")
    @TableField(value = "MD5")
    @ApiModelProperty("MD5加密串;在生成路径验证时，生成验证串")
    private String md5;


    @Excel(name = "内容类型;内容类型：TXT文本文件；DOC文档；DOCX文档；PNG图片；JPG图片等，细化")
    @TableField(value = "MIME_TYPE")
    @ApiModelProperty("内容类型;内容类型：TXT文本文件；DOC文档；DOCX文档；PNG图片；JPG图片等，细化")
    private String mimeType;


    @Excel(name = "是否系统级")
    @TableField(value = "is_sys")
    @ApiModelProperty("是否系统级")
    private Integer isSys;


    @Excel(name = "原始资源ID;dx_original_file表 ORIGINAL_ID 字段")
    @TableField(value = "ORIGINAL_ID")
    @ApiModelProperty("原始资源ID;dx_original_file表 ORIGINAL_ID 字段")
    private Long originalId;


    @Excel(name = "文件后缀;文档的 如：doc，doxc wpf等")
    @TableField(value = "RESOURCE_EXT")
    @ApiModelProperty("文件后缀;文档的 如：doc，doxc wpf等")
    private String resourceExt;


    @Excel(name = "主键;资源ID")
    @TableId(value = "RESOURCE_ID")
    @ApiModelProperty("主键;资源ID")
    private Long resourceId;


    @Excel(name = "资源名称;资源名称")
    @TableField(value = "RESOURCE_NAME")
    @ApiModelProperty("资源名称;资源名称")
    private String resourceName;


    @Excel(name = "目录发布路径;资源文件存放位置路径")
    @TableField(value = "RESOURCE_PATH")
    @ApiModelProperty("目录发布路径;资源文件存放位置路径")
    private String resourcePath;


    @Excel(name = "资源大小;资源文件的大小")
    @TableField(value = "RESOURCE_SIZE")
    @ApiModelProperty("资源大小;资源文件的大小")
    private Long resourceSize;


    @Excel(name = "资源类型;原始资源文件的类型：文本文件、视频文件、音频文件、 图片文件、可执行文件格式类型；以及未知类型；具体可根据后缀名去定义枚举类型；字典")
    @TableField(value = "RESOURCE_TYPE")
    @ApiModelProperty("资源类型;原始资源文件的类型：文本文件、视频文件、音频文件、 图片文件、可执行文件格式类型；以及未知类型；具体可根据后缀名去定义枚举类型；字典")
    private String resourceType;

}
