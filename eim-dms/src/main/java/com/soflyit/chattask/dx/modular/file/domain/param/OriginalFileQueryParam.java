package com.soflyit.chattask.dx.modular.file.domain.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 原始资源文件对象 dx_original_file
 *
 * @author jiaozhishang
 * @date 2023-11-10 14:43:36
 */
@TableName(value = "dx_original_file")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class OriginalFileQueryParam extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "主键;表主键ID")
    @TableField(value = "ORIGINAL_ID")
    @ApiModelProperty("主键;表主键ID")
    private Long originalId;


    @Excel(name = "原始名称;上传文件的")
    @TableField(value = "ORIGINAL_NAME")
    @ApiModelProperty("原始名称;上传文件的")
    private String originalName;


    @Excel(name = "来源类型;分类：数据来源字典表;类型：聊天文件/网盘文件/系统初始化/OA系统对接")
    @TableField(value = "ORIGINAL_TYPE")
    @ApiModelProperty("来源类型;分类：数据来源字典表;类型：聊天文件/网盘文件/系统初始化/OA系统对接")
    private String originalType;


    @Excel(name = "文件类型;原始资源文件的类型：文本文件、视频文件、音频文件、 图片文件、可执行文件格式类型；以及未知类型；具体可根据后缀名去定义枚举类型；字典")
    @TableField(value = "FILE_TYPE")
    @ApiModelProperty("文件类型;原始资源文件的类型：文本文件、视频文件、音频文件、 图片文件、可执行文件格式类型；以及未知类型；具体可根据后缀名去定义枚举类型；字典")
    private String fileType;


    @Excel(name = "内容类型;内容类型：TXT文本文件；DOC文档；DOCX文档等，细化")
    @TableField(value = "MIME_TYPE")
    @ApiModelProperty("内容类型;内容类型：TXT文本文件；DOC文档；DOCX文档等，细化")
    private String mimeType;


    @Excel(name = "文件后缀;原始文件后缀名：doc/docx/txt/avi等")
    @TableField(value = "FILE_EXT")
    @ApiModelProperty("文件后缀;原始文件后缀名：doc/docx/txt/avi等")
    private String fileExt;


    @Excel(name = "文件大小;原始资源文件大小：单位bit")
    @TableField(value = "ORIGINAL_SIZE")
    @ApiModelProperty("文件大小;原始资源文件大小：单位bit")
    private String originalSize;


    @Excel(name = "初始上传路径;文件存放路径位置")
    @TableField(value = "ORIGINAL_PATH")
    @ApiModelProperty("初始上传路径;文件存放路径位置")
    private String originalPath;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;


    @Excel(name = "删除标记;删除标记：1：删除；2：正常")
    @TableField(value = "DELETE_FLAG")
    @ApiModelProperty("删除标记;删除标记：1：删除；2：正常")
    private String deleteFlag;

}
