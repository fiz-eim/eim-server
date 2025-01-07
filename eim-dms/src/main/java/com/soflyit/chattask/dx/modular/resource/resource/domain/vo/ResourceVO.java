package com.soflyit.chattask.dx.modular.resource.resource.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soflyit.chattask.dx.modular.folder.domain.vo.FolderTree;
import com.soflyit.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统资源文档库对象 dx_resource
 *
 * @author soflyit
 * @date 2023-11-07 16:56:57
 */
@ApiModel
@Data
public class ResourceVO implements Serializable {
    private static final long serialVersionUID = 1L;


    @Excel(name = "资源所属目录")
    @ApiModelProperty("资源所属目录ID,上级目录")
    private Long folderParentId;



    @Excel(name = "权限信息")
    @ApiModelProperty("权限信息")
    private String permContent;


    @Excel(name = "路径")
    @ApiModelProperty("路径")
    private String resourcePath;


    @Excel(name = "创建人")
    @ApiModelProperty("创建人")
    private Long createBy;


    @Excel(name = "创建者姓名")
    @ApiModelProperty("创建者姓名")
    private String createNickName;


    @Excel(name = "更新者姓名")
    @ApiModelProperty("更新者姓名")
    private String updateNickName;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;


    @Excel(name = "文档库存储ID;dx_folder 表 FOLDER_ID 字段")
    @ApiModelProperty("文档库存储ID;dx_folder 表 FOLDER_ID 字段")
    private Long folderId;


    @Excel(name = "内容类型;内容类型：TXT文本文件；DOC文档；DOCX文档；PNG图片；JPG图片等，细化")
    @ApiModelProperty("内容类型;内容类型：TXT文本文件；DOC文档；DOCX文档；PNG图片；JPG图片等，细化")
    private String mimeType;


    @Excel(name = "原始资源ID;dx_original_file表 ORIGINAL_ID 字段")
    @ApiModelProperty("原始资源ID;dx_original_file表 ORIGINAL_ID 字段")
    private Long originalId;


    @Excel(name = "文件后缀;文档的 如：doc，doxc wpf等")
    @ApiModelProperty("文件后缀;文档的 如：doc，doxc wpf等")
    private String resourceExt;


    @Excel(name = "主键;资源ID")
    @ApiModelProperty("主键;资源ID")
    private Long resourceId;


    @Excel(name = "资源名称;资源名称")
    @ApiModelProperty("资源名称;资源名称")
    private String resourceName;


    @Excel(name = "目录发布路径;资源文件存放位置路径")
    @ApiModelProperty("目录发布路径;资源文件存放位置路径")
    private String levelPath;

    @Excel(name = "目录发布路径;资源文件存放位置路径")
    @ApiModelProperty("目录发布路径;资源文件存放位置路径")
    private FolderTree levelTree;


    @Excel(name = "资源大小;资源文件的大小")
    @ApiModelProperty("资源大小;资源文件的大小")
    private String resourceSize;


    @Excel(name = "资源类型;原始资源文件的类型：文本文件、视频文件、音频文件、 图片文件、可执行文件格式类型；以及未知类型；具体可根据后缀名去定义枚举类型；字典")
    @ApiModelProperty("资源类型;原始资源文件的类型：文本文件、视频文件、音频文件、 图片文件、可执行文件格式类型；以及未知类型；具体可根据后缀名去定义枚举类型；字典")
    private String resourceType;


    @Excel(name = "更新人")
    @ApiModelProperty("更新人")
    private Long updatedBy;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("更新时间")
    private Date updatedTime;

}
