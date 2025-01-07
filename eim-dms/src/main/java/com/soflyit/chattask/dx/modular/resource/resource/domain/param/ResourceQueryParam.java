package com.soflyit.chattask.dx.modular.resource.resource.domain.param;

import com.soflyit.chattask.dx.common.base.OrderByParam;
import com.soflyit.chattask.dx.common.base.RangeParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统资源文档库对象 dx_resource
 *
 * @author soflyit
 * @date 2023-11-07 16:56:57
 */
@ApiModel
@Data
public class ResourceQueryParam implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("内容类型;内容类型：TXT文本文件；DOC文档；DOCX文档；PNG图片；JPG图片等，细化")
    private OrderByParam<String[]> mimeType;



    @ApiModelProperty("文件后缀;文档的 如：doc，doxc wpf等")
    private OrderByParam<String[]> resourceExt;


    @ApiModelProperty("资源名称;资源名称")
    private OrderByParam<String> resourceName;


    @ApiModelProperty("资源大小;资源文件的大小")
    private OrderByParam<RangeParam<String>> resourceSize;



    @ApiModelProperty("资源类型;原始资源文件的类型：文本文件、视频文件、音频文件、 图片文件、可执行文件格式类型；以及未知类型；具体可根据后缀名去定义枚举类型；字典")
    private OrderByParam<String[]> resourceType;


    @ApiModelProperty("资源大小;资源文件的大小")
    private OrderByParam<RangeParam<String>> createTime;

    @ApiModelProperty("资源大小;资源文件的大小")
    private OrderByParam<RangeParam<String>> updateTime;
}
