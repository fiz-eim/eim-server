package com.soflyit.chattask.dx.modular.folder.my.domain.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.web.domain.BaseEntity;
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
public class MyFolderEditParam extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @Excel(name = "主键")
    @TableField(value = "ID")
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
