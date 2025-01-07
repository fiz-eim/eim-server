package com.soflyit.chattask.dx.modular.operate.domain.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.soflyit.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文档库的变更记录对象 dx_operate_record
 *
 * @author soflyit
 * @date 2023-11-06 14:53:26
 */
@ApiModel
@Data
public class OperateRecordAddParam implements Serializable {
    private static final long serialVersionUID = 1L;



    @Excel(name = "目录ID;dx_folder 的 folder_id")
    @TableField(value = "forder_id")
    @ApiModelProperty("目录ID;dx_folder 的 folder_id")
    private Long folderId;


    @Excel(name = "具体操作分类;create_file.创建文件；create_folder.创建目录；具体要依据操作类型和操作的主体来区分")
    @TableField(value = "operate_body")
    @ApiModelProperty("具体操作分类;create_file.创建文件；create_folder.创建目录；具体要依据操作类型和操作的主体来区分")
    private String operateBody;


    @Excel(name = "操作具体概述;推荐使用json模板格式进行映射生成：")
    @TableField(value = "operate_data")
    @ApiModelProperty("操作具体概述;推荐使用json模板格式进行映射生成：")
    private String operateData;


    @Excel(name = "操作对象", readConverterExp = "目=标")
    @TableField(value = "operate_target")
    @ApiModelProperty("操作对象（目标）;操作对象（目标）：Spring Data JPA中文文档.pdf")
    private String operateTarget;


    @Excel(name = "操作类型;upload 上传；down 下载;create 创建；update修改；delete 删除；open 查看等等")
    @TableField(value = "operate_type")
    @ApiModelProperty("操作类型;upload 上传；down 下载;create 创建；update修改；delete 删除；open 查看等等")
    private String operateType;


    @Excel(name = "资源ID;dx_resource 的 resource_id")
    @TableField(value = "resource_id")
    @ApiModelProperty("资源ID;dx_resource 的 resource_id")
    private Long resourceId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;

}
