package com.soflyit.chattask.dx.modular.folder.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soflyit.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文档库存储目录对象 dx_folder
 *
 * @author soflyit
 * @date 2023-11-08 14:54:35
 */
@ApiModel
@Data
public class FolderVO implements Serializable {
    private static final long serialVersionUID = 1L;


    @Excel(name = "创建人;创建人")
    @ApiModelProperty("创建人;创建人")
    private Long createdBy;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间;创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("创建时间;创建时间")
    private Date createdTime;


    @Excel(name = "删除标记;删除标记：1：删除；2：正常")
    @ApiModelProperty("删除标记;删除标记：1：删除；2：正常")
    private Long deleteFlag;


    @Excel(name = "文件数;目录中文件的统计数：是否有必要维护？")
    @ApiModelProperty("文件数;目录中文件的统计数：是否有必要维护？")
    private Long fileTotal;


    @Excel(name = "主键")
    @ApiModelProperty("主键")
    private Long folderId;


    @Excel(name = "层级;格式：[FOLDER_ID1,FOLDER_ID2,FOLDER_ID3,FOLDER_ID4]")
    @ApiModelProperty("层级;格式：[FOLDER_ID1,FOLDER_ID2,FOLDER_ID3,FOLDER_ID4]")
    private String folderLevel;

    @Excel(name = "层级;格式：[FOLDER_ID1,FOLDER_ID2,FOLDER_ID3,FOLDER_ID4]")
    @ApiModelProperty("层级;格式：[FOLDER_ID1,FOLDER_ID2,FOLDER_ID3,FOLDER_ID4]")
    private String folderPath;


    @Excel(name = "目录名称")
    @ApiModelProperty("目录名称")
    private String folderName;


    @Excel(name = "上级目录ID;默认规范 ： 0为顶级目录")
    @ApiModelProperty("上级目录ID;默认规范 ： 0为顶级目录")
    private Long folderParentId;


    @Excel(name = "目录数;子级目录统计数：是否有必要维护？")
    @ApiModelProperty("目录数;子级目录统计数：是否有必要维护？")
    private Long folderTotal;


    @Excel(name = "乐观锁")
    @ApiModelProperty("乐观锁")
    private Long revision;


    @Excel(name = "租户号")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "更新人;修改人ID")
    @ApiModelProperty("更新人;修改人ID")
    private Long updatedBy;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间;最新修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty("更新时间;最新修改时间")
    private Date updatedTime;

}
